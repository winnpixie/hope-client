package io.github.alerithe.client.utilities.graphics.drawing;

import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class BufferedDrawDevice implements DrawDevice {
    private static final ByteBuffer VBO = BufferUtils.createByteBuffer((4 + (4 * 3)) * 65536); // C4UB_V3F
    private static final IntBuffer INDICES = BufferUtils.createIntBuffer(65536);
    private static final IntBuffer COUNTS = BufferUtils.createIntBuffer(65536);

    private int mode;
    private int index;

    @Override
    public void drawRect(double left, double top, double right, double bottom, int color) {
        int r = (color >> 16) & 255;
        int g = (color >> 8) & 255;
        int b = color & 255;
        int a = (color >> 24) & 255;
        if (a == 0) {
            return;
        }

        color(r, g, b, a);
        position(left, top, 0.0);
        color(r, g, b, a);
        position(left, bottom, 0.0);
        color(r, g, b, a);
        position(right, bottom, 0.0);
        color(r, g, b, a);
        position(right, top, 0.0);

        push(4);
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

        color(r, g, b, a);
        position(startX, startY, startZ);
        color(r, g, b, a);
        position(endX, endY, endZ);

        push(2);
    }

    private void color(int r, int g, int b, int a) {
        VBO
                .put((byte) r)
                .put((byte) g)
                .put((byte) b)
                .put((byte) a);
    }

    private void position(double x, double y, double z) {
        VBO
                .putFloat((float) x)
                .putFloat((float) y)
                .putFloat((float) z);
    }

    private void push(int count) {
        INDICES.put((index++) * count);
        COUNTS.put(count);
    }

    public void begin(int mode) {
        this.mode = mode;
        this.index = 0;
    }

    public void end() {
        GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
        GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);

        VBO.flip();
        GL11.glInterleavedArrays(GL11.GL_C4UB_V3F, 16, VBO);
        VBO.clear();

        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);

        INDICES.flip();
        COUNTS.flip();
        GL14.glMultiDrawArrays(mode, INDICES, COUNTS);
        INDICES.clear();
        COUNTS.clear();

        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();

        GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
        GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
    }
}
