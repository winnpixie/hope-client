package io.github.alerithe.client.ui.click.elements.impl.input;

import io.github.alerithe.client.ui.click.elements.Element;
import io.github.alerithe.client.ui.click.elements.handlers.ElementEventListener;
import io.github.alerithe.client.ui.click.elements.styling.ElementStyle;
import io.github.alerithe.client.ui.click.elements.styling.text.TextAlignment;
import io.github.alerithe.client.utilities.MathHelper;
import io.github.alerithe.client.utilities.graphics.VisualHelper;

public class Slider extends Element {
    private double value;
    private final double minimum;
    private final double maximum;

    private boolean dragging;

    public Slider(float x, float y, float width, float height, double value) {
        this(x, y, width, height, value, 0.0, 1.0);
    }

    public Slider(float x, float y, float width, float height, double initialValue, double minimum, double maximum) {
        super(x, y, width, height);

        this.value = initialValue;
        this.minimum = minimum;
        this.maximum = maximum;

        addListener(new ElementEventListener() {
            @Override
            public void onLeftClick(int mouseX, int mouseY) {
                dragging = true;
            }

            @Override
            public void onMouseUp(int mouseX, int mouseY, int button) {
                if (button == 0) dragging = false;
            }
        });

        getNormalStyle().textStyle.setAlignment(TextAlignment.CENTER);
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public void onValueChanged(double oldValue, double newValue) {
    }

    @Override
    public void onDraw(int mouseX, int mouseY, float partialTicks) {
        if (dragging) {
            double oldValue = value;
            float relativeMouseX = mouseX - getX();

            value = (minimum + MathHelper.clamp(relativeMouseX / getWidth(), 0f, 1f) * (maximum - minimum));
            onValueChanged(oldValue, value);
        }

        float lineWidth = 1f;
        float lineSize = 8f;
        float halfSize = lineWidth / 2f;

        float normalized = (float) ((value - minimum) / (maximum - minimum));
        float xOffset = normalized * getWidth();
        if (xOffset + lineWidth > getWidth()) xOffset = getWidth() - lineWidth;

        ElementStyle style = isHovered() ? getNormalStyle() : getHoveredStyle();

        // Vertical bar
        VisualHelper.MC_GFX.drawSquare(getX() + xOffset,
                getY() + (getHeight() / 2f) - (lineSize / 2f),
                lineWidth,
                lineSize,
                style.getForegroundColor());

        // Horizontal Line
        VisualHelper.MC_GFX.drawSquare(getX() + 1,
                getY() + (getHeight() / 2f) - halfSize,
                getWidth() - 2,
                lineWidth,
                style.getForegroundColor());
    }
}
