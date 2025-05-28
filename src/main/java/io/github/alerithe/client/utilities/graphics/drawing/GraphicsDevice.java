package io.github.alerithe.client.utilities.graphics.drawing;

public interface GraphicsDevice {
    default void drawBorderedSquare(double x, double y, double width, double height, double borderSize, int color, int borderColor) {
        drawBorderedRect(x, y, x + width, y + height, borderSize, color, borderColor);
    }

    default void drawBorderedRect(double left, double top, double right, double bottom, double borderSize, int color, int borderColor) {
        drawRect(left, top, right, bottom, color);

        drawRect(left, top - borderSize, right + borderSize, top, borderColor); // Top
        drawRect(left - borderSize, bottom, right, bottom + borderSize, borderColor); // Bottom
        drawRect(left - borderSize, top - borderSize, left, bottom, borderColor); // Left
        drawRect(right, top, right + borderSize, bottom + borderSize, borderColor); // Right
    }

    default void drawSquare(double x, double y, double width, double height, int color) {
        drawRect(x, y, x + width, y + height, color);
    }

    void drawRect(double left, double top, double right, double bottom, int color);

    void drawLine(double left, double top, double right, double bottom, int color);

    default void drawPrism(double startX, double startY, double startZ, double endX, double endY, double endZ, int color) {
    }

    default void drawLine(double startX, double startY, double startZ, double endX, double endY, double endZ, int color) {
    }
}
