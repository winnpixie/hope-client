package io.github.alerithe.client.features.modules.impl.visual;

import io.github.alerithe.client.Client;
import io.github.alerithe.client.events.bus.Subscribe;
import io.github.alerithe.client.events.game.EventDraw;
import io.github.alerithe.client.events.game.EventInput;
import io.github.alerithe.client.events.game.EventPacket;
import io.github.alerithe.client.events.game.EventTick;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.features.properties.impl.BooleanProperty;
import io.github.alerithe.client.utilities.*;
import io.github.alerithe.client.utilities.graphics.VisualHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
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

public class HUD extends Module {
    private final BooleanProperty frameRate = new BooleanProperty("FrameRate", new String[]{"fps"}, true);
    private final BooleanProperty latency = new BooleanProperty("Latency", new String[]{"ping"}, true);
    private final BooleanProperty realPing = new BooleanProperty("RealPing", new String[0], true);
    private final BooleanProperty tickRate = new BooleanProperty("TickRate", new String[]{"tps"}, true);
    private final BooleanProperty arrayList = new BooleanProperty("ArrayList", new String[0], true);
    private final BooleanProperty potionEffects = new BooleanProperty("PotionEffects", new String[]{"potions", "effects"}, true);
    private final BooleanProperty coordinates = new BooleanProperty("Coordinates", new String[]{"coords"}, true);
    private final BooleanProperty leftClicks = new BooleanProperty("LeftClicksPerSecond", new String[]{"lcps", "cps"}, true);
    private final BooleanProperty rightClicks = new BooleanProperty("RightClicksPerSecond", new String[]{"rcps"}, true);

    private final Comparator<Module> moduleComparator = Comparator.comparingDouble(module ->
            -VisualHelper.TXT.getStringWidth(module.getName()));
    private final Comparator<PotionEffect> potionComparator = Comparator.comparingDouble(effect -> {
        Potion potion = Potion.potionTypes[effect.getPotionID()];
        String text = String.format("%s %s", I18n.format(potion.getName()), formatPotionEffect(effect));

        return -VisualHelper.TXT.getStringWidth(text);
    });
    private final List<Long> leftClickTimes = new ArrayList<>();
    private final List<Long> rightTickTimes = new ArrayList<>();
    private final List<Long> serverTickTimes = new CopyOnWriteArrayList<>();

    private boolean waitingForPong;
    private long pingRequestTime;
    private long pongResponseTime;

    public HUD() {
        super("HUD", new String[0], Type.VISUAL);

        getPropertyManager().add(frameRate);
        getPropertyManager().add(latency);
        getPropertyManager().add(realPing);
        getPropertyManager().add(tickRate);
        getPropertyManager().add(leftClicks);
        getPropertyManager().add(rightClicks);
        getPropertyManager().add(arrayList);
        getPropertyManager().add(coordinates);
        getPropertyManager().add(potionEffects);
    }

    @Override
    public void onEnable() {
        leftClickTimes.clear();
        rightTickTimes.clear();
        serverTickTimes.clear();
    }

    @Subscribe
    public void onOverlayDraw(EventDraw.Overlay event) {
        ScaledResolution display = VisualHelper.getDisplay();

        if (!GameHelper.getSettings().showDebugInfo) {
            drawInfo(display);
            drawArrayList(display);
        }

        drawCoordinates(display);
        drawPotionEffects(display);
    }

    private void drawInfo(ScaledResolution display) {
        String text = String.format("\247o%s \247f%s \247r", Client.NAME, Client.BUILD);

        // FPS
        if (frameRate.getValue()) {
            text += String.format(" \2477[\247f%d FPS\2477]", Minecraft.getDebugFPS());
        }

        // Ping
        if (latency.getValue()) {
            text += String.format(" \2477[\247f%dms", NetworkHelper.getPing());

            if (realPing.getValue()) {
                text += String.format(" : %dms", pongResponseTime);
            }

            text += "\2477]";
        }

        // TPS
        if (tickRate.getValue()) {
            text += " \2477[\247f";

            if (serverTickTimes.size() < 2) {
                text += "??.??";
            } else {
                long oldestTick = serverTickTimes.get(0);
                long latestTick = serverTickTimes.get(serverTickTimes.size() - 1);
                double tps = 20.0 / MathHelper.max((latestTick - oldestTick) / (1000.0 * (serverTickTimes.size() - 1)), 1.0);

                text += String.format("%.2f", tps);
            }

            text += " TPS\2477]";
        }

        // CPS
        if (leftClicks.getValue() || rightClicks.getValue()) {
            text += " \2477[\247f";

            if (leftClicks.getValue()) {
                text += leftClickTimes.size();
            }

            if (rightClicks.getValue()) {
                if (leftClicks.getValue()) {
                    text += " : ";
                }

                text += rightTickTimes.size();
            }

            text += "\2477]";
        }

        VisualHelper.TXT.drawStringWithShadow(text, 1, 1, Client.ACCENT_COLOR);
    }

    private void drawArrayList(ScaledResolution display) {
        if (!arrayList.getValue()) {
            return;
        }

        List<Module> enabled = new ArrayList<>();
        for (Module module : Client.MODULE_MANAGER.getElements()) {
            if (!module.isEnabled()
                    || module.getVisibility().getValue()) {
                continue;
            }

            enabled.add(module);
        }
        enabled.sort(moduleComparator);

        float y = 2;
        for (Module module : enabled) {
            float textWidth = VisualHelper.TXT.getStringWidth(module.getName());
            float x = display.getScaledWidth() - textWidth;

            VisualHelper.GFX.drawSquare(x - 3, y - 2, textWidth + 3, VisualHelper.TXT.getFontHeight() + 3,
                    0x42000000);

            VisualHelper.TXT.drawStringWithShadow(module.getName(), x - 1, y, -1);
            y += VisualHelper.TXT.getFontHeight() + 3;
        }
    }

    private void drawCoordinates(ScaledResolution display) {
        if (!coordinates.getValue()) {
            return;
        }

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

        VisualHelper.TXT.drawStringWithShadow(builder.toString(), 2, display.getScaledHeight() - getLowerOffset(), -1);
    }

    private String formatPotionEffect(PotionEffect effect) {
        int amp = effect.getAmplifier() + 1;
        return String.format("\2476%s \2477%s", amp > 0 && amp < 11
                ? I18n.format("enchantment.level." + amp) : amp, Potion.getDurationString(effect));
    }

    private void drawPotionEffects(ScaledResolution display) {
        if (!potionEffects.getValue()) {
            return;
        }

        List<PotionEffect> effects = new ArrayList<>(EntityHelper.getUser().getActivePotionEffects());
        effects.sort(potionComparator);

        float y = getLowerOffset();

        GlStateManager.disableLighting();
        for (PotionEffect effect : effects) {
            Potion potion = Potion.potionTypes[effect.getPotionID()];
            String label = formatPotionEffect(effect);

            GameHelper.getGame().getTextureManager().bindTexture(GuiContainer.inventoryBackground);
            int potionIndex = potion.getStatusIconIndex();
            Gui.drawTexturedModalRect2(display.getScaledWidth() - VisualHelper.TXT.getStringWidth(label) - 20,
                    display.getScaledHeight() - y - 5, potionIndex % 8 * 18.0, 198 + potionIndex / 8 * 18.0, 18, 18);

            VisualHelper.TXT.drawStringWithShadow(label,
                    display.getScaledWidth() - VisualHelper.TXT.getStringWidth(label) - 1f,
                    display.getScaledHeight() - y, potion.getLiquidColor() | 0xFF000000);

            y += 18f;
        }
    }

    private static float getLowerOffset() {
        return 1f + (GameHelper.getGame().ingameGUI.getChatGUI().getChatOpen() ?
                VisualHelper.TXT.getFontHeight() * 2f + 4f : VisualHelper.TXT.getFontHeight());
    }

    @Subscribe
    private void onEndTick(EventTick.End event) {
        if (!event.isInGame()) {
            return;
        }

        if (realPing.getValue()) {
            if (!waitingForPong) {
                NetworkHelper.sendPacket(new C16PacketClientStatus(C16PacketClientStatus.EnumState.REQUEST_STATS));
                pingRequestTime = Stopwatch.getNow();

                waitingForPong = true;
            }
        }

        List<Long> oldClicks = new ArrayList<>();
        for (long ms : leftClickTimes) {
            if (Stopwatch.getNow() - ms >= 1000) {
                oldClicks.add(ms);
            }
        }
        leftClickTimes.removeAll(oldClicks);

        oldClicks.clear();
        for (long ms : rightTickTimes) {
            if (Stopwatch.getNow() - ms >= 1000) {
                oldClicks.add(ms);
            }
        }
        rightTickTimes.removeAll(oldClicks);

        if (serverTickTimes.size() > 20) {
            serverTickTimes.remove(0);
        }
    }

    @Subscribe
    private void onLeftClick(EventInput.LeftClick event) {
        leftClickTimes.add(Stopwatch.getNow());
    }

    @Subscribe
    private void onRightClick(EventInput.RightClick event) {
        rightTickTimes.add(Stopwatch.getNow());
    }

    @Subscribe
    private void onPacketRead(EventPacket.Read event) {
        if (event.getPacket() instanceof S02PacketLoginSuccess) {
            serverTickTimes.clear();
        } else if (event.getPacket() instanceof S03PacketTimeUpdate) {
            serverTickTimes.add(Stopwatch.getNow());
        } else if (event.getPacket() instanceof S37PacketStatistics) {
            if (waitingForPong) {
                pongResponseTime = Stopwatch.getNow() - pingRequestTime;

                waitingForPong = false;
            }
        }
    }
}
