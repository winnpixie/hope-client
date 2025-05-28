package io.github.alerithe.client.ui.click.elements.impl.input;

import io.github.alerithe.client.ui.click.elements.Element;
import io.github.alerithe.client.ui.click.elements.handlers.EventHandler;
import io.github.alerithe.client.ui.click.elements.styling.ElementStyle;
import io.github.alerithe.client.ui.click.elements.styling.text.TextAlignment;
import io.github.alerithe.client.ui.click.elements.styling.text.TextPosition;

public class CheckBox extends Element {
    private boolean checked;

    public final Button checkButton;

    public ElementStyle checkedStyle = new ElementStyle(null);
    public ElementStyle uncheckedStyle = new ElementStyle(null);

    public CheckBox(String text, float x, float y, float width, float height, boolean initialState) {
        super(x, y, width, height);

        setText(text);
        this.checked = initialState;

        float size = 8f;
        checkButton = new Button(null, getWidth() - size - (size / 4f), getHeight() / 2f - (size / 2f), size, size);
        checkButton.addHandler(new EventHandler() {
            @Override
            public void onLeftClick(int mouseX, int mouseY) {
                setChecked(!checked);

                onValueChanged(!checked, checked);
            }
        });

        getNormalStyle().textStyle.setAlignment(TextAlignment.LEFT);
        getNormalStyle().textStyle.setPosition(TextPosition.MIDDLE);
        updateVisuals();

        addChild(checkButton);
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;

        updateVisuals();
    }

    public void onValueChanged(boolean oldValue, boolean newValue) {
    }

    private void updateVisuals() {
        checkButton.setNormalStyle(checked ? checkedStyle : uncheckedStyle);
    }
}
