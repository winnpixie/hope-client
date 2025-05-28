package io.github.alerithe.client.ui.click.elements.impl.input;

import io.github.alerithe.client.ui.click.elements.Element;
import io.github.alerithe.client.ui.click.elements.handlers.EventHandler;
import net.minecraft.util.ChatAllowedCharacters;
import org.lwjgl.input.Keyboard;

public class TextArea extends Element {
    public TextArea(String initialText, float x, float y, float width, float height) {
        super(x, y, width, height);

        setText(initialText);

        addHandler(new EventHandler() {
            @Override
            public void onKeyPressed(char charCode, int keyCode) {
                String oldText = getText();

                if (keyCode == Keyboard.KEY_BACK || keyCode == Keyboard.KEY_DELETE) {
                    if (!oldText.isEmpty()) setText(oldText.substring(0, oldText.length() - 1));
                } else if (isAllowedCharacter(charCode)) {
                    setText(oldText + charCode);
                }

                if (!oldText.equals(getText())) onValueChanged(oldText, getText());
            }
        });

        getNormalStyle().textStyle.setLineWrap(true);
    }

    public void onValueChanged(String oldValue, String newValue) {
    }

    @Override
    public void setText(String text) {
        if (text == null) text = "";

        super.setText(text);
    }

    private boolean isAllowedCharacter(char c) {
        return ChatAllowedCharacters.isAllowedCharacter(c);
    }
}
