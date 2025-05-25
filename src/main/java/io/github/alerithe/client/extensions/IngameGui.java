package io.github.alerithe.client.extensions;

import io.github.alerithe.events.EventBus;
import io.github.alerithe.client.events.game.EventDraw;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import org.lwjgl.opengl.GL11;

public class IngameGui extends GuiIngame {
    public IngameGui(Minecraft mcIn) {
        super(mcIn);
    }

    @Override
    public void renderGameOverlay(float partialTicks) {
        super.renderGameOverlay(partialTicks);

        GL11.glPushMatrix();
        EventBus.dispatch(new EventDraw.Overlay(partialTicks));
        GL11.glColor4f(1, 1, 1, 1);
        GL11.glPopMatrix();
    }
}
