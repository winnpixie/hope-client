package io.github.alerithe.client.utilities.graphics.drawing;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

public class MinecraftDrawDevice implements DrawDevice {
    @Override
    public void drawRect(double left, double top, double right, double bottom, int color) {
        int r = (color >> 16) & 255;
        int g = (color >> 8) & 255;
        int b = color & 255;
        int a = (color >> 24) & 255;
        if (a == 0) {
            return;
        }

        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);

        Tessellator tess = Tessellator.getInstance();
        WorldRenderer wr = tess.getWorldRenderer();
        wr.begin(GL11.GL_TRIANGLE_FAN, DefaultVertexFormats.POSITION_COLOR);
        wr.pos(left, top, 0.0).color(r, g, b, a).endVertex();
        wr.pos(left, bottom, 0.0).color(r, g, b, a).endVertex();
        wr.pos(right, bottom, 0.0).color(r, g, b, a).endVertex();
        wr.pos(right, top, 0.0).color(r, g, b, a).endVertex();
        tess.draw();

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

        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);

        Tessellator tess = Tessellator.getInstance();
        WorldRenderer wr = tess.getWorldRenderer();
        // Bottom Square
        wr.begin(GL11.GL_LINE_LOOP, DefaultVertexFormats.POSITION_COLOR);
        wr.pos(startX, startY, startZ).color(r, g, b, a).endVertex();
        wr.pos(endX, startY, startZ).color(r, g, b, a).endVertex();
        wr.pos(endX, startY, endZ).color(r, g, b, a).endVertex();
        wr.pos(startX, startY, endZ).color(r, g, b, a).endVertex();
        tess.draw();

        // Top Square
        wr.begin(GL11.GL_LINE_LOOP, DefaultVertexFormats.POSITION_COLOR);
        wr.pos(startX, endY, startZ).color(r, g, b, a).endVertex();
        wr.pos(endX, endY, startZ).color(r, g, b, a).endVertex();
        wr.pos(endX, endY, endZ).color(r, g, b, a).endVertex();
        wr.pos(startX, endY, endZ).color(r, g, b, a).endVertex();
        tess.draw();

        // Columns
        wr.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);
        wr.pos(startX, startY, startZ).color(r, g, b, a).endVertex();
        wr.pos(startX, endY, startZ).color(r, g, b, a).endVertex();
        wr.pos(endX, startY, startZ).color(r, g, b, a).endVertex();
        wr.pos(endX, endY, startZ).color(r, g, b, a).endVertex();
        wr.pos(endX, startY, endZ).color(r, g, b, a).endVertex();
        wr.pos(endX, endY, endZ).color(r, g, b, a).endVertex();
        wr.pos(startX, startY, endZ).color(r, g, b, a).endVertex();
        wr.pos(startX, endY, endZ).color(r, g, b, a).endVertex();
        tess.draw();

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

        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);

        Tessellator tess = Tessellator.getInstance();
        WorldRenderer wr = tess.getWorldRenderer();
        wr.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);
        wr.pos(startX, startY, startZ).color(r, g, b, a).endVertex();
        wr.pos(endX, endY, endZ).color(r, g, b, a).endVertex();
        tess.draw();

        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
    }
}
