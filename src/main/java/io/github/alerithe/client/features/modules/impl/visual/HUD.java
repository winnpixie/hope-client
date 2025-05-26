package io.github.alerithe.client.features.modules.impl.visual;

import io.github.alerithe.client.Client;
import io.github.alerithe.client.events.game.EventDraw;
import io.github.alerithe.client.events.game.EventInput;
import io.github.alerithe.client.events.game.EventPacket;
import io.github.alerithe.client.events.game.EventTick;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.features.properties.impl.BooleanProperty;
import io.github.alerithe.client.utilities.MathHelper;
import io.github.alerithe.client.utilities.MsTimer;
import io.github.alerithe.client.utilities.Wrapper;
import io.github.alerithe.client.utilities.graphics.VisualHelper;
import io.github.alerithe.events.CallOrder;
import io.github.alerithe.events.Register;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.network.NetworkPlayerInfo;
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
    private final BooleanProperty fps = new BooleanProperty("FPS", new String[]{"showfps"}, true);
    private final BooleanProperty ping = new BooleanProperty("Ping", new String[]{"showping"}, true);
    private final BooleanProperty realPing = new BooleanProperty("RealPing", new String[]{"showrealping"}, true);
    private final BooleanProperty tps = new BooleanProperty("TPS", new String[]{"showtps"}, true);
    private final BooleanProperty arrayList = new BooleanProperty("ArrayList", new String[]{"showenabled"}, true);
    private final BooleanProperty potions = new BooleanProperty("Effects", new String[]{"showpotions"}, true);
    private final BooleanProperty coords = new BooleanProperty("Coordinates", new String[]{"coords", "showcoords"}, true);
    private final BooleanProperty cps = new BooleanProperty("ClicksPerSecond", new String[]{"cps", "showcps"}, true);
    private final BooleanProperty rcps = new BooleanProperty("RightClicksPerSecond", new String[]{"rcps", "showrcps"}, true);

    private final Comparator<Module> moduleSorter = Comparator.comparingDouble(module -> -VisualHelper.MC_FONT.getStringWidth(module.getName()));
    private final Comparator<PotionEffect> potionSorter = Comparator.comparingDouble(effect -> {
        Potion potion = Potion.potionTypes[effect.getPotionID()];

        int amp = effect.getAmplifier() + 1;
        String text = String.format("%s \2476%s \2477%s", I18n.format(potion.getName()), amp > 0 && amp < 11
                ? I18n.format("enchantment.level." + amp) : amp, Potion.getDurationString(effect));

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

        getPropertyManager().add(fps);
        getPropertyManager().add(ping);
        getPropertyManager().add(realPing);
        getPropertyManager().add(tps);
        getPropertyManager().add(cps);
        getPropertyManager().add(rcps);
        getPropertyManager().add(arrayList);
        getPropertyManager().add(coords);
        getPropertyManager().add(potions);
    }

    @Override
    public void onEnable() {
        leftClicks.clear();
        rightClicks.clear();
        ticks.clear();
    }

    @Register(CallOrder.UNIMPORTANT)
    private void onOverlayDraw(EventDraw.Overlay event) {
        ScaledResolution display = VisualHelper.getDisplay();

        if (!Wrapper.getSettings().showDebugInfo) {
            // Client Info
            {
                String text = String.format("\247o%s\247r", Client.NAME);

                // FPS
                if (fps.getValue()) text += String.format(" \2477[\247f%d FPS\2477]", Minecraft.getDebugFPS());

                // Ping
                if (ping.getValue()) {
                    text += " \2477[\247f";
                    NetworkPlayerInfo npi = Wrapper.getNetInfo(Wrapper.getPlayer().getGameProfile().getId());
                    if (npi != null) {
                        text += String.format("%dms", npi.getResponseTime());
                    }

                    if (realPing.getValue()) {
                        text += String.format(" : %dms", statResponseTime);
                    }

                    text += "\2477]";
                }

                // TPS
                if (tps.getValue() && ticks.size() > 1) {
                    long firstTick = ticks.get(0);
                    long mostRecentTick = ticks.get(ticks.size() - 1);
                    double tickRate = 20.0 / MathHelper.max((mostRecentTick - firstTick) / (1000.0 * (ticks.size() - 1)), 1.0);

                    text += String.format(" \2477[\247f%.2f TPS\2477]", tickRate);
                }

                // CPS
                if (cps.getValue() || rcps.getValue()) {
                    text += " \2477[\247f";
                    if (cps.getValue()) text += leftClicks.size();

                    if (rcps.getValue()) {
                        if (cps.getValue()) text += " : ";

                        text += rightClicks.size();
                    }
                    text += "\2477]";
                }

                VisualHelper.MC_FONT.drawStringWithShadow(text, 1, 1, Client.ACCENT_COLOR);
            }

            // ArrayList
            if (arrayList.getValue()) {
                int y = 2;
                List<Module> enabled = Client.MODULE_MANAGER.getElements().stream().filter(Module::isEnabled)
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
        }

        int chatOffset = Wrapper.getGame().ingameGUI.getChatGUI().getChatOpen() ? 24 : 10;
        // Potions
        if (potions.getValue()) {
            int y = chatOffset + 4;
            List<PotionEffect> effects = Wrapper.getPlayer().getActivePotionEffects().stream().sorted(potionSorter)
                    .collect(Collectors.toList());
            for (PotionEffect effect : effects) {
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                GlStateManager.disableLighting();
                Potion potion = Potion.potionTypes[effect.getPotionID()];

                int amp = effect.getAmplifier() + 1;
                String text = String.format("\2476%s \2477%s", amp > 0 && amp < 11
                        ? I18n.format("enchantment.level." + amp) : amp, Potion.getDurationString(effect));

                Wrapper.getGame().getTextureManager().bindTexture(GuiInventory.inventoryBackground);
                int potionIndex = potion.getStatusIconIndex();
                Gui.drawTexturedModalRect2(display.getScaledWidth() - VisualHelper.MC_FONT.getStringWidth(text) - 20,
                        display.getScaledHeight() - y - 5, potionIndex % 8 * 18, 198 + potionIndex / 8 * 18, 18, 18);

                VisualHelper.MC_FONT.drawStringWithShadow(text,
                        display.getScaledWidth() - VisualHelper.MC_FONT.getStringWidth(text) - 1,
                        display.getScaledHeight() - y, potion.getLiquidColor());
                y += 16;
            }
        }

        // Coordinates
        if (coords.getValue()) {
            String text = String.format("\2477[\247r %.0f \2477/\247r %.0f \2477/\247r %.0f \2477]",
                    Wrapper.getPlayer().posX, Wrapper.getPlayer().posY, Wrapper.getPlayer().posZ);
            VisualHelper.MC_FONT.drawStringWithShadow(text, 2, display.getScaledHeight() - chatOffset, -1);
        }
    }

    @Register
    private void onTick(EventTick event) {
        if (!event.isInGame()) return;

        if (realPing.getValue()) {
            if (!awaitingStatResponse) {
                Wrapper.sendPacket(new C16PacketClientStatus(C16PacketClientStatus.EnumState.REQUEST_STATS));
                statRequestTime = MsTimer.getNow();
                awaitingStatResponse = true;
            }
        }

        List<Long> old = new ArrayList<>();
        for (long ms : leftClicks) {
            if (MsTimer.getNow() - ms >= 1000) {
                old.add(ms);
            }
        }
        leftClicks.removeAll(old);

        old.clear();
        for (long ms : rightClicks) {
            if (MsTimer.getNow() - ms >= 1000) {
                old.add(ms);
            }
        }
        rightClicks.removeAll(old);

        if (ticks.size() > 20) {
            ticks.remove(0);
        }
    }

    @Register
    private void onLeftClick(EventInput.LeftClick event) {
        leftClicks.add(MsTimer.getNow());
    }

    @Register
    private void onRightClick(EventInput.RightClick event) {
        rightClicks.add(MsTimer.getNow());
    }

    @Register
    private void onPacketRead(EventPacket.Read event) {
        if (event.getPacket() instanceof S02PacketLoginSuccess) {
            ticks.clear();
        }

        if (event.getPacket() instanceof S03PacketTimeUpdate) {
            ticks.add(MsTimer.getNow());
        }

        if (event.getPacket() instanceof S37PacketStatistics) {
            if (awaitingStatResponse) {
                statResponseTime = MsTimer.getNow() - statRequestTime;
                awaitingStatResponse = false;
            }
        }
    }
}
