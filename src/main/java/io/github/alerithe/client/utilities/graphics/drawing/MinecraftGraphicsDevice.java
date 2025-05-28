package io.github.alerithe.client.utilities.graphics.drawing;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

public class MinecraftGraphicsDevice implements GraphicsDevice {
    @Override
    public void drawRect(double left, double top, double right, double bottom, int color) {
        if (right < left) {
            double swp = left;
            left = right;
            right = swp;
        }

        if (bottom < top) {
            double swp = top;
            top = bottom;
            bottom = swp;
        }

        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);

        float r = (color >> 16 & 255) / 255f;
        float g = (color >> 8 & 255) / 255f;
        float b = (color & 255) / 255f;
        float a = (color >> 24 & 255) / 255f;
        GlStateManager.color(r, g, b, a);

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldrenderer.pos(left, bottom, 0.0).endVertex();
        worldrenderer.pos(right, bottom, 0.0).endVertex();
        worldrenderer.pos(right, top, 0.0).endVertex();
        worldrenderer.pos(left, top, 0.0).endVertex();
        tessellator.draw();

        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    @Override
    public void drawLine(double left, double top, double right, double bottom, int color) {
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);

        float r = (color >> 16 & 255) / 255f;
        float g = (color >> 8 & 255) / 255f;
        float b = (color & 255) / 255f;
        float a = (color >> 24 & 255) / 255f;
        GlStateManager.color(r, g, b, a);

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);
        worldrenderer.pos(left, top, 0.0).endVertex();
        worldrenderer.pos(right, bottom, 0.0).endVertex();
        tessellator.draw();

        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    @Override
    public void drawPrism(double startX, double startY, double startZ, double endX, double endY, double endZ, int color) {
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);

        float r = (color >> 16 & 255) / 255f;
        float g = (color >> 8 & 255) / 255f;
        float b = (color & 255) / 255f;
        float a = (color >> 24 & 255) / 255f;
        GlStateManager.color(r, g, b, a);

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(GL11.GL_LINE_LOOP, DefaultVertexFormats.POSITION);
        worldrenderer.pos(startX, startY, startZ).endVertex();
        worldrenderer.pos(endX, startY, startZ).endVertex();
        worldrenderer.pos(endX, startY, endZ).endVertex();
        worldrenderer.pos(startX, startY, endZ).endVertex();
        tessellator.draw();

        worldrenderer.begin(GL11.GL_LINE_LOOP, DefaultVertexFormats.POSITION);
        worldrenderer.pos(startX, endY, startZ).endVertex();
        worldrenderer.pos(endX, endY, startZ).endVertex();
        worldrenderer.pos(endX, endY, endZ).endVertex();
        worldrenderer.pos(startX, endY, endZ).endVertex();
        tessellator.draw();

        worldrenderer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);
        worldrenderer.pos(startX, startY, startZ).endVertex();
        worldrenderer.pos(startX, endY, startZ).endVertex();
        worldrenderer.pos(endX, startY, startZ).endVertex();
        worldrenderer.pos(endX, endY, startZ).endVertex();
        worldrenderer.pos(endX, startY, endZ).endVertex();
        worldrenderer.pos(endX, endY, endZ).endVertex();
        worldrenderer.pos(startX, startY, endZ).endVertex();
        worldrenderer.pos(startX, endY, endZ).endVertex();
        tessellator.draw();

        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    @Override
    public void drawLine(double startX, double startY, double startZ, double endX, double endY, double endZ, int color) {
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);

        float r = (color >> 16 & 255) / 255f;
        float g = (color >> 8 & 255) / 255f;
        float b = (color & 255) / 255f;
        float a = (color >> 24 & 255) / 255f;
        GlStateManager.color(r, g, b, a);

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);
        worldrenderer.pos(startX, startY, startZ).endVertex();
        worldrenderer.pos(endX, endY, endZ).endVertex();
        tessellator.draw();

        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
}
