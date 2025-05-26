package io.github.alerithe.client.utilities.graphics.drawing;

public interface GraphicsDevice {
    default void drawBorderedSquare(float x, float y, float width, float height, float borderSize, int color, int borderColor) {
        drawBorderedRect(x, y, x + width, y + height, borderSize, color, borderColor);
    }

    default void drawBorderedRect(float left, float top, float right, float bottom, float borderSize, int color, int borderColor) {
        drawRect(left, top, right, bottom, color);

        drawRect(left, top - borderSize, right + borderSize, top, borderColor); // Top
        drawRect(left - borderSize, bottom, right, bottom + borderSize, borderColor); // Bottom
        drawRect(left - borderSize, top - borderSize, left, bottom, borderColor); // Left
        drawRect(right, top, right + borderSize, bottom + borderSize, borderColor); // Right
    }

    default void drawSquare(float x, float y, float width, float height, int color) {
        drawRect(x, y, x + width, y + height, color);
    }

    void drawRect(float left, float top, float right, float bottom, int color);
}
