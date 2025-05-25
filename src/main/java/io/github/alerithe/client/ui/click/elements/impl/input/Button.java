package io.github.alerithe.client.ui.click.elements.impl.input;

import io.github.alerithe.client.ui.click.elements.Element;
import io.github.alerithe.client.ui.click.elements.styling.text.TextAlignment;
import io.github.alerithe.client.ui.click.elements.styling.text.TextPosition;

public class Button extends Element {
    public Button(String text, float x, float y, float width, float height) {
        super(x, y, width, height);

        setText(text);

        getNormalStyle().setBackgroundColor(0xFF696969);
        getNormalStyle().textStyle.setAlignment(TextAlignment.CENTER);
        getNormalStyle().textStyle.setPosition(TextPosition.MIDDLE);

        getHoveredStyle().setBackgroundColor(0xFF424242);
    }
}
