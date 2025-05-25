package io.github.alerithe.client.utilities.graphics;

public interface FontRenderer {
    float getFontHeight();

    float getStringWidth(String text);

    default void drawString(String text, float x, float y, int color) {
        drawString(text, x, y, color, false);
    }

    default void drawStringWithShadow(String text, float x, float y, int color) {
        drawString(text, x, y, color, true);
    }

    void drawString(String text, float x, float y, int color, boolean shadow);
}
