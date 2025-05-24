package io.github.alerithe.client.ui.click.elements.impl;

import io.github.alerithe.client.ui.click.elements.styling.TextAlignment;
import io.github.alerithe.client.utilities.VisualHelper;

public class CheckBox extends Button {
    private boolean checked;

    public CheckBox(String text, int x, int y, int width, int height) {
        super(text, x, y, width, height);

        getStyle().textAlignment = TextAlignment.LEFT;
    }

    @Override
    public void onClick(int mouseX, int mouseY, int button) {
        if (button == 0) {
            checked = !checked;
            onValueChanged(checked);
        }
    }

    public void onValueChanged(boolean newValue) {
    }

    @Override
    public void onDraw(int mouseX, int mouseY, float partialTicks) {
        float size = getWidth() / 8f;
        VisualHelper.drawSquare(getX() + getWidth() - size - 1, getY() + (getHeight() / 2f) - (size / 2f), size, size,
                checked ? getStyle().foregroundColor : getStyle().backgroundColor);
    }
}
