package io.github.alerithe.client.features.modules.impl.visual;

import io.github.alerithe.client.Client;
import io.github.alerithe.client.events.game.EventDraw;
import io.github.alerithe.client.events.game.EventInput;
import io.github.alerithe.client.events.game.EventPacket;
import io.github.alerithe.client.events.game.EventTick;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.features.properties.impl.BooleanProperty;
import io.github.alerithe.client.utilities.*;
import io.github.alerithe.client.utilities.graphics.VisualHelper;
import io.github.alerithe.events.impl.Subscribe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.network.login.server.S02PacketLoginSuccess;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.network.play.server.S03PacketTimeUpdate;
import net.minecraft.network.play.server.S37PacketStatistics;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class HUD extends Module {
    private final BooleanProperty frameRate = new BooleanProperty("FrameRate", new String[]{"fps"}, true);
    private final BooleanProperty latency = new BooleanProperty("Latency", new String[]{"ping"}, true);
    private final BooleanProperty realPing = new BooleanProperty("RealPing", new String[0], true);
    private final BooleanProperty tickRate = new BooleanProperty("TickRate", new String[]{"tps"}, true);
    private final BooleanProperty arrayList = new BooleanProperty("ArrayList", new String[0], true);
    private final BooleanProperty potionEffects = new BooleanProperty("PotionEffects", new String[]{"potions", "effects"}, true);
    private final BooleanProperty coordinates = new BooleanProperty("Coordinates", new String[]{"coords"}, true);
    private final BooleanProperty lCps = new BooleanProperty("LeftClicksPerSecond", new String[]{"lcps", "cps"}, true);
    private final BooleanProperty rCps = new BooleanProperty("RightClicksPerSecond", new String[]{"rcps"}, true);

    private final Comparator<Module> moduleSorter = Comparator.comparingDouble(module -> -VisualHelper.MC_FONT.getStringWidth(module.getName()));
    private final Comparator<PotionEffect> potionSorter = Comparator.comparingDouble(effect -> {
        Potion potion = Potion.potionTypes[effect.getPotionID()];
        String text = String.format("%s %s", I18n.format(potion.getName()), formatPotionEffect(effect));

        return -VisualHelper.MC_FONT.getStringWidth(text);
    });
    private final List<Long> leftClicks = new CopyOnWriteArrayList<>();
    private final List<Long> rightClicks = new CopyOnWriteArrayList<>();
    private final List<Long> ticks = new CopyOnWriteArrayList<>();

    private boolean awaitingStatResponse;
    private long statRequestTime;
    private long statResponseTime;

    public HUD() {
        super("HUD", new String[0], Type.VISUAL);

        getPropertyManager().add(frameRate);
        getPropertyManager().add(latency);
        getPropertyManager().add(realPing);
        getPropertyManager().add(tickRate);
        getPropertyManager().add(lCps);
        getPropertyManager().add(rCps);
        getPropertyManager().add(arrayList);
        getPropertyManager().add(coordinates);
        getPropertyManager().add(potionEffects);
    }

    @Override
    public void onEnable() {
        leftClicks.clear();
        rightClicks.clear();
        ticks.clear();
    }

    @Subscribe
    private void onOverlayDraw(EventDraw.Overlay event) {
        ScaledResolution display = VisualHelper.getDisplay();

        if (!GameHelper.getSettings().showDebugInfo) {
            drawInfo(display);
            drawArrayList(display);
        }

        drawCoordinates(display);
        drawPotionEffects(display);
    }

    private void drawInfo(ScaledResolution display) {
        String text = String.format("\247o%s\247r", Client.NAME);

        // FPS
        if (frameRate.getValue()) text += String.format(" \2477[\247f%d FPS\2477]", Minecraft.getDebugFPS());

        // Ping
        if (latency.getValue()) {
            text += String.format(" \2477[\247f%dms", NetworkHelper.getPing());

            if (realPing.getValue()) text += String.format(" : %dms", statResponseTime);

            text += "\2477]";
        }

        // TPS
        if (tickRate.getValue() && ticks.size() > 1) {
            long oldestTick = ticks.get(0);
            long latestTick = ticks.get(ticks.size() - 1);
            double tickRate = 20.0 / MathHelper.max((latestTick - oldestTick) / (1000.0 * (ticks.size() - 1)), 1.0);

            text += String.format(" \2477[\247f%.2f TPS\2477]", tickRate);
        }

        // CPS
        if (lCps.getValue() || rCps.getValue()) {
            text += " \2477[\247f";
            if (lCps.getValue()) text += leftClicks.size();

            if (rCps.getValue()) {
                if (lCps.getValue()) text += " : ";

                text += rightClicks.size();
            }

            text += "\2477]";
        }

        VisualHelper.MC_FONT.drawStringWithShadow(text, 1, 1, Client.ACCENT_COLOR);
    }

    private void drawArrayList(ScaledResolution display) {
        if (!arrayList.getValue()) return;

        float y = 2;
        List<Module> enabled = Client.MODULE_MANAGER.getChildren().stream().filter(Module::isEnabled)
                .filter(module -> !module.hidden.getValue()).sorted(moduleSorter).collect(Collectors.toList());
        for (Module module : enabled) {
            float textWidth = VisualHelper.MC_FONT.getStringWidth(module.getName());
            float x = display.getScaledWidth() - textWidth;

            VisualHelper.MC_GFX.drawSquare(x - 4, y - 2, textWidth + 4, VisualHelper.MC_FONT.getFontHeight() + 4,
                    Client.ACCENT_COLOR);
            VisualHelper.MC_GFX.drawSquare(x - 3, y - 2, textWidth + 3, VisualHelper.MC_FONT.getFontHeight() + 3,
                    0xFF111111);

            VisualHelper.MC_FONT.drawStringWithShadow(module.getName(), x - 1, y, -1);
            y += 12;
        }
    }

    private void drawCoordinates(ScaledResolution display) {
        if (!coordinates.getValue()) return;

        float chatOffset = GameHelper.getGame().ingameGUI.getChatGUI().getChatOpen() ? 24 : 10;

        double uX = EntityHelper.getUser().posX;
        double uY = EntityHelper.getUser().posY;
        double uZ = EntityHelper.getUser().posZ;

        int dimId = WorldHelper.getWorld().provider.getDimensionId();
        StringBuilder builder = new StringBuilder("\2477[ \247r"); // Hear me out...
        if (dimId < 1) {
            double dimFactor = dimId == -1 ? 8.0 : 0.125;
            char dimColor = dimId == -1 ? 'a' : 'c';

            builder.append(String.format("%.1f(\247%c%.1f\247r) \2477/\247r %.1f \2477/\247r %.1f(\247%c%.1f\247r)",
                    uX, dimColor, uX * dimFactor, uY, uZ, dimColor, uZ * dimFactor));
        } else {
            builder.append(String.format("\247d %.1f \2477/\247d %.1f \2477/\247d %.1f",
                    uX, uY, uZ));
        }

        builder.append(" \2477]");

        VisualHelper.MC_FONT.drawStringWithShadow(builder.toString(), 2, display.getScaledHeight() - chatOffset, -1);
    }

    private String formatPotionEffect(PotionEffect effect) {
        int amp = effect.getAmplifier() + 1;
        return String.format("\2476%s \2477%s", amp > 0 && amp < 11
                ? I18n.format("enchantment.level." + amp) : amp, Potion.getDurationString(effect));
    }

    private void drawPotionEffects(ScaledResolution display) {
        if (!potionEffects.getValue()) return;

        float chatOffset = GameHelper.getGame().ingameGUI.getChatGUI().getChatOpen() ? 24 : 10;
        float y = chatOffset + 4;
        List<PotionEffect> effects = EntityHelper.getUser().getActivePotionEffects().stream().sorted(potionSorter)
                .collect(Collectors.toList());
        for (PotionEffect effect : effects) {
            GlStateManager.color(1f, 1f, 1f, 1f);
            GlStateManager.disableLighting();
            Potion potion = Potion.potionTypes[effect.getPotionID()];

            String label = formatPotionEffect(effect);

            GameHelper.getGame().getTextureManager().bindTexture(GuiInventory.inventoryBackground);
            int potionIndex = potion.getStatusIconIndex();
            Gui.drawTexturedModalRect2(display.getScaledWidth() - VisualHelper.MC_FONT.getStringWidth(label) - 20,
                    display.getScaledHeight() - y - 5, potionIndex % 8 * 18, 198 + potionIndex / 8 * 18, 18, 18);

            VisualHelper.MC_FONT.drawStringWithShadow(label,
                    display.getScaledWidth() - VisualHelper.MC_FONT.getStringWidth(label) - 1,
                    display.getScaledHeight() - y, potion.getLiquidColor());

            y += 16f;
        }
    }

    @Subscribe
    private void onEndTick(EventTick.End event) {
        if (!event.isInGame()) return;

        if (realPing.getValue()) {
            if (!awaitingStatResponse) {
                NetworkHelper.sendPacket(new C16PacketClientStatus(C16PacketClientStatus.EnumState.REQUEST_STATS));
                statRequestTime = Stopwatch.getNow();
                awaitingStatResponse = true;
            }
        }

        List<Long> old = new ArrayList<>();
        for (long ms : leftClicks) {
            if (Stopwatch.getNow() - ms >= 1000) old.add(ms);
        }
        leftClicks.removeAll(old);

        old.clear();
        for (long ms : rightClicks) {
            if (Stopwatch.getNow() - ms >= 1000) old.add(ms);
        }
        rightClicks.removeAll(old);

        if (ticks.size() > 20) ticks.remove(0);
    }

    @Subscribe
    private void onLeftClick(EventInput.LeftClick event) {
        leftClicks.add(Stopwatch.getNow());
    }

    @Subscribe
    private void onRightClick(EventInput.RightClick event) {
        rightClicks.add(Stopwatch.getNow());
    }

    @Subscribe
    private void onPacketRead(EventPacket.Read event) {
        if (event.getPacket() instanceof S02PacketLoginSuccess) {
            ticks.clear();
        }

        if (event.getPacket() instanceof S03PacketTimeUpdate) {
            ticks.add(Stopwatch.getNow());
        }

        if (event.getPacket() instanceof S37PacketStatistics) {
            if (awaitingStatResponse) {
                statResponseTime = Stopwatch.getNow() - statRequestTime;
                awaitingStatResponse = false;
            }
        }
    }
}
