package io.github.alerithe.client.utilities.graphics.drawing;

import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

public class ImmediateDrawDevice implements DrawDevice {
    @Override
    public void drawRect(double left, double top, double right, double bottom, int color) {
        int r = (color >> 16) & 255;
        int g = (color >> 8) & 255;
        int b = color & 255;
        int a = (color >> 24) & 255;
        if (a == 0) {
            return;
        }

        GlStateManager.color(r / 255f, g / 255f, b / 255f, a / 255f);

        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);

        GL11.glBegin(GL11.GL_TRIANGLE_FAN);
        GL11.glVertex2d(left, top);
        GL11.glVertex2d(left, bottom);
        GL11.glVertex2d(right, bottom);
        GL11.glVertex2d(right, top);
        GL11.glEnd();

        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
    }

    @Override
    public void drawPrism(double startX, double startY, double startZ, double endX, double endY, double endZ, int color) {
        int r = (color >> 16) & 255;
        int g = (color >> 8) & 255;
        int b = color & 255;
        int a = (color >> 24) & 255;
        if (a == 0) {
            return;
        }

        GlStateManager.color(r / 255f, g / 255f, b / 255f, a / 255f);

        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);

        // Bottom Square
        GL11.glBegin(GL11.GL_LINE_LOOP);
        GL11.glVertex3d(startX, startY, startZ);
        GL11.glVertex3d(endX, startY, startZ);
        GL11.glVertex3d(endX, startY, endZ);
        GL11.glVertex3d(startX, startY, endZ);
        GL11.glEnd();

        // Top Square
        GL11.glBegin(GL11.GL_LINE_LOOP);
        GL11.glVertex3d(startX, endY, startZ);
        GL11.glVertex3d(endX, endY, startZ);
        GL11.glVertex3d(endX, endY, endZ);
        GL11.glVertex3d(startX, endY, endZ);
        GL11.glEnd();

        // Columns
        GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex3d(startX, startY, startZ);
        GL11.glVertex3d(startX, endY, startZ);
        GL11.glVertex3d(endX, startY, startZ);
        GL11.glVertex3d(endX, endY, startZ);
        GL11.glVertex3d(endX, startY, endZ);
        GL11.glVertex3d(endX, endY, endZ);
        GL11.glVertex3d(startX, startY, endZ);
        GL11.glVertex3d(startX, endY, endZ);
        GL11.glEnd();

        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
    }

    @Override
    public void drawLine(double startX, double startY, double startZ, double endX, double endY, double endZ, int color) {
        int r = (color >> 16) & 255;
        int g = (color >> 8) & 255;
        int b = color & 255;
        int a = (color >> 24) & 255;
        if (a == 0) {
            return;
        }

        GlStateManager.color(r / 255f, g / 255f, b / 255f, a / 255f);

        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);

        GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex3d(startX, startY, startZ);
        GL11.glVertex3d(endX, endY, endZ);
        GL11.glEnd();

        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
    }
}
