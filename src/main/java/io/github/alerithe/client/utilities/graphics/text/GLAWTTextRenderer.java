package io.github.alerithe.client.utilities.graphics.text;

import io.github.alerithe.client.utilities.MathHelper;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.awt.font.LineMetrics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Map;
import java.util.Objects;
import java.util.WeakHashMap;

public class GLAWTTextRenderer implements TextRenderer {
    private static final GraphicsConfiguration DISPLAY;
    private static final BufferedImage TEMPLATE;

    private final Map<Integer, BakedImage> bakery = new WeakHashMap<>();

    private final Font font;
    private final Graphics2D context;

    static {
        DISPLAY = GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getDefaultScreenDevice()
                .getDefaultConfiguration();

        TEMPLATE = DISPLAY.createCompatibleImage(2, 2, Transparency.TRANSLUCENT);
    }

    public GLAWTTextRenderer(Font font) {
        this.font = font;
        this.context = TEMPLATE.createGraphics();

        context.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        context.setFont(font);
    }

    @Override
    public float getFontHeight() {
        return (float) font.getStringBounds(" ", context.getFontRenderContext()).getHeight() / 2f;
    }

    @Override
    public float getStringWidth(String text) {
        return (float) font.getStringBounds(stripControlCodes(text), context.getFontRenderContext()).getWidth() / 2f;
    }

    @Override
    public void drawString(String text, float x, float y, int baseColor, boolean hasShadow) {
        BakedImage baked = bakery.computeIfAbsent(Objects.hash(text, baseColor, hasShadow),
                k -> bake(text, baseColor, hasShadow));

        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);

        GlStateManager.bindTexture(baked.textureId);

        GlStateManager.pushMatrix();

        // downscaling = free antialiasing!
        GlStateManager.translate(x, y, 0f);
        GlStateManager.scale(0.5f, 0.5f, 1f);
        GlStateManager.translate(-x, -y, 0f);

        GlStateManager.color(1f, 1f, 1f, 1f);

        int width = baked.width;
        int height = baked.height;
        GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
        GL11.glTexCoord2f(0f, 0f);
        GL11.glVertex2f(x, y);
        GL11.glTexCoord2f(0f, 1f);
        GL11.glVertex2f(x, y + height);
        GL11.glTexCoord2f(1f, 0f);
        GL11.glVertex2f(x + width, y);
        GL11.glTexCoord2f(1f, 1f);
        GL11.glVertex2f(x + width, y + height);
        GL11.glEnd();

        GlStateManager.popMatrix();
        GlStateManager.disableBlend();
    }

    private BakedImage bake(String text, int baseColor, boolean shadow) {
        Rectangle2D bounds = font.getStringBounds(text, context.getFontRenderContext());
        LineMetrics metrics = font.getLineMetrics(text, context.getFontRenderContext());

        int width = (int) MathHelper.max(MathHelper.ceil(bounds.getWidth()), 1) + 1;
        int height = (int) MathHelper.max(MathHelper.ceil(bounds.getHeight()), 1) + 1;
        BufferedImage image = DISPLAY.createCompatibleImage(width, height, Transparency.TRANSLUCENT);

        Graphics2D graphics = image.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        Color originalColor = new Color(baseColor, true);
        graphics.setColor(originalColor);

        Font currentFont = font;
        graphics.setFont(currentFont);

        int length = text.length();
        float x = 0f;
        float y = metrics.getAscent();
        int next = 0;
        int len = 0;
        for (int i = 0; i < length; i++) {
            int newFontStyle = -1;
            Color newColor = null;

            char ch = text.charAt(i);
            if (ch == '\247' && i + 1 < length) {
                char code = text.charAt(i + 1);
                newFontStyle = getStyle(code, font.getStyle());
                newColor = getColor(code, originalColor);
            }

            if (newFontStyle > -1 || newColor != null) {
                String segment = text.substring(i - len, i);

                if (shadow) {
                    Graphics2D shadowGraphics = (Graphics2D) graphics.create();
                    shadowGraphics.setColor(changeBrightness(shadowGraphics.getColor(), 0.1f));
                    shadowGraphics.drawString(segment, x + 1f, y + 1f);
                    shadowGraphics.dispose();
                }

                graphics.drawString(segment, x, y);

                x += (float) currentFont.getStringBounds(segment, graphics.getFontRenderContext()).getWidth();
                next = i + 2;
                len = 0;

                if (newFontStyle > -1) {
                    currentFont = font.deriveFont(newFontStyle);
                    graphics.setFont(currentFont);
                }

                if (newColor != null) {
                    graphics.setColor(newColor);
                }

                i++;
                continue;
            }

            len++;
        }

        if (len > 0) {
            String segment = text.substring(next, next + len);

            if (shadow) {
                Graphics2D shadowGraphics = (Graphics2D) graphics.create();
                shadowGraphics.setColor(changeBrightness(shadowGraphics.getColor(), 0.1f));
                shadowGraphics.drawString(segment, x + 1f, y + 1f);
                shadowGraphics.dispose();
            }

            graphics.drawString(segment, x, y);
        }

        graphics.dispose();

        return new BakedImage(image);
    }

    private static String stripControlCodes(String str) {
        int length = str.length();
        char[] stripped = new char[length];

        int len = 0;
        for (int i = 0; i < length; i++) {
            char ch = str.charAt(i);
            if (ch == '\u00A7' && i + 1 < length) {
                char c = Character.toLowerCase(str.charAt(i + 1));
                if ((c >= 'a' && c <= 'f')
                        || (c >= '0' && c <= '9')
                        || (c >= 'k' && c <= 'o')
                        || c == 'r') {
                    i++;
                    continue;
                }
            }

            stripped[len++] = ch;
        }

        return new String(stripped, 0, len);
    }

    private static Color changeBrightness(Color color, float brightness) {
        return new Color(
                (color.getRed() * brightness) / 255f,
                (color.getGreen() * brightness) / 255f,
                (color.getBlue() * brightness) / 255f,
                color.getAlpha() / 255f
        );
    }

    private static int uploadImage(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int[] pixels = new int[width * height];
        image.getRGB(0, 0, width, height, pixels, 0, width);

        ByteBuffer buffer = ByteBuffer.allocateDirect(width * height * 4)
                .order(ByteOrder.nativeOrder());
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

    private static int getStyle(char c, int originalStyle) {
        switch (c) {
            case 'l':
                return Font.BOLD;
            case 'o':
                return Font.ITALIC;
            case 'r':
                return originalStyle;
            default:
                return -1;
        }
    }

    // TODO: Return vanilla color values
    private static Color getColor(char c, Color base) {
        switch (c) {
            case '0':
                return Color.BLACK;
            case '1':
                return Color.BLUE;
            case '2':
                return Color.GREEN;
            case '3':
                return Color.BLUE;
            case '4':
                return Color.RED;
            case '5':
                return Color.MAGENTA;
            case '6':
                return Color.ORANGE;
            case '7':
                return Color.GRAY;
            case '8':
                return Color.DARK_GRAY;
            case '9':
                return Color.BLUE;
            case 'a':
                return Color.GREEN;
            case 'b':
                return Color.CYAN;
            case 'c':
                return Color.RED;
            case 'd':
                return Color.MAGENTA;
            case 'e':
                return Color.YELLOW;
            case 'f':
                return Color.WHITE;
            case 'r':
                return base;
            default:
                return null;
        }
    }

    private static class BakedImage {
        private final int textureId;
        private final int width;
        private final int height;

        BakedImage(BufferedImage image) {
            this.textureId = uploadImage(image);
            this.width = image.getWidth();
            this.height = image.getHeight();

            // release resources tied to image
            image.flush();
        }

        @Override
        protected void finalize() throws Throwable {
            // On GC: release texture from VRAM
            GL11.glDeleteTextures(textureId);
        }
    }
}
