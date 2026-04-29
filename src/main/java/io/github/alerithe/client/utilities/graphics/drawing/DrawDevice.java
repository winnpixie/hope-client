package io.github.alerithe.client.utilities.graphics.drawing;

public interface DrawDevice {
    void drawRect(double left, double top, double right, double bottom, int color);

    void drawLine(double startX, double startY, double startZ, double endX, double endY, double endZ, int color);

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

    default void drawSimpleBorderedSquare(double x, double y, double width, double height, double borderSize, int color, int borderColor) {
        drawSimpleBorderedRect(x, y, x + width, y + height, borderSize, color, borderColor);
    }

    default void drawSimpleBorderedRect(double left, double top, double right, double bottom, double borderSize, int color, int borderColor) {
        drawRect(left - borderSize, top - borderSize, right + borderSize, bottom + borderSize, borderColor);

        drawRect(left, top, right, bottom, color);
    }

    default void drawSquare(double x, double y, double width, double height, int color) {
        drawRect(x, y, x + width, y + height, color);
    }

    default void drawLine(double startX, double startY, double endX, double endY, int color) {
        drawLine(startX, startY, 0.0, endX, endY, 0.0, color);
    }

    default void drawPrism(double startX, double startY, double startZ, double endX, double endY, double endZ, int color) {
    }
}
