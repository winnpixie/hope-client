package io.github.alerithe.client.ui.click.elements.impl;

import io.github.alerithe.client.ui.click.elements.Element;

public class Root extends Element {
    public Root(int x, int y, int width, int height) {
        super(x, y, width, height);

        getRenderer().showBackground = false;
        getRenderer().showText = false;
    }
}
