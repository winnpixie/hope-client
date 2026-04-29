package io.github.alerithe.client.utilities.graphics.text.awt;

import io.github.alerithe.client.utilities.MathHelper;

import java.awt.*;
import java.awt.font.LineMetrics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

class ImageBakery {
    private ImageBakery() {
    }

    static BakedImage bake(Font font, Graphics2D context, String text, int baseColor, boolean shadow) {
        Rectangle2D bounds = font.getStringBounds(text, context.getFontRenderContext());
        LineMetrics metrics = font.getLineMetrics(text, context.getFontRenderContext());

        int width = (int) MathHelper.max(MathHelper.ceil(bounds.getWidth()), 1) + 1;
        int height = (int) MathHelper.max(MathHelper.ceil(bounds.getHeight()), 1) + 1;
        BufferedImage image = AWTHelper.DISPLAY.createCompatibleImage(width, height, Transparency.TRANSLUCENT);

        Graphics2D graphics = image.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
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
                    shadowGraphics.setColor(newBrightness(shadowGraphics.getColor(), 0.1f));
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
                    graphics.setColor(newAlpha(newColor, originalColor.getAlpha()));
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
                shadowGraphics.setColor(newBrightness(shadowGraphics.getColor(), 0.1f));
                shadowGraphics.drawString(segment, x + 1f, y + 1f);
                shadowGraphics.dispose();
            }

            graphics.drawString(segment, x, y);
        }

        graphics.dispose();

        return new BakedImage(image);
    }

    private static Color newAlpha(Color color, int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }

    private static Color newBrightness(Color color, float brightness) {
        return new Color(
                (color.getRed() * brightness) / 255f,
                (color.getGreen() * brightness) / 255f,
                (color.getBlue() * brightness) / 255f,
                color.getAlpha() / 255f
        );
    }

    private static int getStyle(char c, int original) {
        switch (c) {
            case 'l':
                return Font.BOLD;
            case 'o':
                return Font.ITALIC;
            case 'r':
                return original;
            default:
                return -1;
        }
    }

    // TODO: Return vanilla color values
    private static Color getColor(char c, Color base) {
        switch (c) {
            case '0':
                return AWTHelper.BLACK;
            case '1':
                return AWTHelper.DARK_BLUE;
            case '2':
                return AWTHelper.DARK_GREEN;
            case '3':
                return AWTHelper.DARK_AQUA;
            case '4':
                return AWTHelper.DARK_RED;
            case '5':
                return AWTHelper.DARK_PURPLE;
            case '6':
                return AWTHelper.GOLD;
            case '7':
                return AWTHelper.GRAY;
            case '8':
                return AWTHelper.DARK_GRAY;
            case '9':
                return AWTHelper.BLUE;
            case 'a':
                return AWTHelper.GREEN;
            case 'b':
                return AWTHelper.CYAN;
            case 'c':
                return AWTHelper.RED;
            case 'd':
                return AWTHelper.MAGENTA;
            case 'e':
                return AWTHelper.YELLOW;
            case 'f':
                return AWTHelper.WHITE;
            case 'r':
                return base;
            default:
                return null;
        }
    }
}
