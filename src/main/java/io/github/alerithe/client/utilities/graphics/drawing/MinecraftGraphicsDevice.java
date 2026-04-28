package io.github.alerithe.client.utilities.graphics.drawing;

import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class MinecraftGraphicsDevice implements GraphicsDevice {
    private final ByteBuffer buffer = ByteBuffer.allocateDirect((4 * 3 * 8) + (4 * 8))
            .order(ByteOrder.nativeOrder());

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

        color(r, g, b, a);
        position(left, top, 0.0);
        color(r, g, b, a);
        position(left, bottom, 0.0);
        color(r, g, b, a);
        position(right, bottom, 0.0);
        color(r, g, b, a);
        position(right, top, 0.0);

        draw(GL11.GL_TRIANGLE_FAN, 4);

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

        // Bottom Square
        color(r, g, b, a);
        position(startX, startY, startZ);
        color(r, g, b, a);
        position(endX, startY, startZ);
        color(r, g, b, a);
        position(endX, startY, endZ);
        color(r, g, b, a);
        position(startX, startY, endZ);

        draw(GL11.GL_LINE_LOOP, 4);

        // Top Square
        color(r, g, b, a);
        position(startX, endY, startZ);
        color(r, g, b, a);
        position(endX, endY, startZ);
        color(r, g, b, a);
        position(endX, endY, endZ);
        color(r, g, b, a);
        position(startX, endY, endZ);

        draw(GL11.GL_LINE_LOOP, 4);

        // Columns
        color(r, g, b, a);
        position(startX, startY, startZ);
        color(r, g, b, a);
        position(startX, endY, startZ);
        color(r, g, b, a);
        position(endX, startY, startZ);
        color(r, g, b, a);
        position(endX, endY, startZ);
        color(r, g, b, a);
        position(endX, startY, endZ);
        color(r, g, b, a);
        position(endX, endY, endZ);
        color(r, g, b, a);
        position(startX, startY, endZ);
        color(r, g, b, a);
        position(startX, endY, endZ);

        draw(GL11.GL_LINES, 8);

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

        color(r, g, b, a);
        position(startX, startY, startZ);
        color(r, g, b, a);
        position(endX, endY, endZ);

        draw(GL11.GL_LINES, 2);

        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
    }

    private void position(double x, double y, double z) {
        buffer
                .putFloat((float) x)
                .putFloat((float) y)
                .putFloat((float) z);
    }

    private void color(int r, int g, int b, int a) {
        buffer
                .put((byte) r)
                .put((byte) g)
                .put((byte) b)
                .put((byte) a);
    }

    private void draw(int mode, int count) {
        GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
        GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);

        buffer.flip();
        GL11.glInterleavedArrays(GL11.GL_C4UB_V3F, 16, buffer);
        buffer.clear();

        GL11.glDrawArrays(mode, 0, count);

        GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
        GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
    }
}
