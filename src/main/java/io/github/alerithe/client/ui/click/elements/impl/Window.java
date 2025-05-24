package io.github.alerithe.client.ui.click.elements.impl;

import io.github.alerithe.client.ui.click.elements.Element;

public class Window extends Element {
    public Window(String title, int x, int y, int width, int height) {
        super(x, y, width, height);

        setText(title);

        getController().draggable = true;

        getController().closeable = true;
        getController().expanded = false;
    }
}
