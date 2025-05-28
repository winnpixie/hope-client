package io.github.alerithe.client.extensions;

import io.github.alerithe.client.Client;
import io.github.alerithe.client.events.game.EventDraw;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.renderer.GlStateManager;

public class IngameGui extends GuiIngame {
    public IngameGui(Minecraft mcIn) {
        super(mcIn);
    }

    @Override
    public void renderGameOverlay(float partialTicks) {
        super.renderGameOverlay(partialTicks);

        GlStateManager.pushMatrix();
        GlStateManager.color(1f, 1f, 1f, 1f);
        Client.EVENT_BUS.post(new EventDraw.Overlay(partialTicks));
        GlStateManager.popMatrix();
    }
}
