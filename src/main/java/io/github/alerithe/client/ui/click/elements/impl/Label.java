package io.github.alerithe.client.ui.click.elements.impl;

import io.github.alerithe.client.ui.click.elements.Element;

public class Label extends Element {
    public Label(String text, int x, int y, int width, int height) {
        super(x, y, width, height);

        setText(text);
    }
}
