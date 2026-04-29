package io.github.alerithe.client.utilities.graphics.text.awt;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

class BakedImage {
    final int textureId;
    final int width;
    final int height;

    BakedImage(BufferedImage image) {
        this.textureId = uploadImage(image);
        this.width = image.getWidth();
        this.height = image.getHeight();

        // release resources tied to image
        image.flush();
    }

    private static int uploadImage(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int[] pixels = new int[width * height];
        image.getRGB(0, 0, width, height, pixels, 0, width);

        ByteBuffer buffer = BufferUtils.createByteBuffer(4 * (width * height));
        for (int iy = 0; iy < height; iy++) {
            for (int ix = 0; ix < width; ix++) {
                int color = pixels[ix + iy * width];
                buffer.put((byte) ((color >> 16) & 255));
                buffer.put((byte) ((color >> 8) & 255));
                buffer.put((byte) (color & 255));
                buffer.put((byte) ((color >> 24) & 255));
            }
        }
        buffer.flip();

        int texture = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);

        return texture;
    }

    @Override
    protected void finalize() throws Throwable {
        // release texture data on GC
        GL11.glDeleteTextures(textureId);
    }
}