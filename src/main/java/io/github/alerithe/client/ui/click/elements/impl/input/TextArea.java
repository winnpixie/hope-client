package io.github.alerithe.client.ui.click.elements.impl.input;

import io.github.alerithe.client.ui.click.elements.Element;
import org.lwjgl.input.Keyboard;

public class TextArea extends Element {
    public TextArea(String text, float x, float y, float width, float height) {
        super(x, y, width, height);

        setText(text);

        getNormalStyle().textStyle.setLineWrap(true);
    }

    @Override
    public void onKeyPressed(char charCode, int keyCode) {
        if (keyCode == Keyboard.KEY_BACK || keyCode == Keyboard.KEY_DELETE) {
            if (!getText().isEmpty()) setText(getText().substring(0, getText().length() - 1));
        } else {
            if (charCode != '\r') setText(getText() + charCode);
        }
    }
}
