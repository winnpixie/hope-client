package io.github.alerithe.client.utilities.graphics.drawing;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

public class BufferedDrawDevice implements DrawDevice {
    private static final ByteBuffer VBO = ByteBuffer.allocateDirect((4 + (4 * 3)) * 8192) // C4UB_V3F
            .order(ByteOrder.nativeOrder());
    private static final IntBuffer INDICES = ByteBuffer.allocateDirect(4 * 8192)
            .order(ByteOrder.nativeOrder()).asIntBuffer();
    private static final IntBuffer COUNTS = ByteBuffer.allocateDirect(4 * 8192)
            .order(ByteOrder.nativeOrder()).asIntBuffer();

    private int index;
    private int count;

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

        push();
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

        push();
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

    private void push() {
        INDICES.put((index++) * count);
        COUNTS.put(count);
    }

    public void begin(int vertexCount) {
        this.index = 0;
        this.count = vertexCount;
    }

    public void end(int mode) {
        GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
        GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);

        VBO.flip();
        GL11.glInterleavedArrays(GL11.GL_C4UB_V3F, 16, VBO);
        VBO.clear();

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        INDICES.flip();
        COUNTS.flip();
        GL14.glMultiDrawArrays(mode, INDICES, COUNTS);
        INDICES.clear();
        COUNTS.clear();

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_TEXTURE_2D);

        GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
        GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
    }
}
