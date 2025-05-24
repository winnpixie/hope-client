package io.github.alerithe.client.ui.click.elements.impl;

import io.github.alerithe.client.ui.click.elements.Element;
import io.github.alerithe.client.utilities.VisualHelper;

public class Slider extends Element {
    private boolean dragging;
    private int dragX;

    public float value;

    public Slider(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void onClick(int mouseX, int mouseY, int button) {
        if (button == 0) {
            dragging = true;
        }
    }

    @Override
    public void onRelease(int mouseX, int mouseY, int button) {
        if (button == 0) dragging = false;
    }

    @Override
    public void onDraw(int mouseX, int mouseY, float partialTicks) {
        VisualHelper.drawSquare(
                getX() + 1,
                (getY() + getHeight() / 2f) - 1,
                getWidth() - 2, 2, getStyle().foregroundColor);

        float offset = (value * getWidth());
        VisualHelper.drawSquare(getX() + offset, getY() + 1, 1, getHeight() - 2, getStyle().foregroundColor);
    }
}
