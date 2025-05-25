package io.github.alerithe.client.ui.click.elements.handlers.impl;

import io.github.alerithe.client.ui.click.elements.Element;
import io.github.alerithe.client.ui.click.elements.handlers.EventHandler;

public class Collapsible extends EventHandler {
    private final Element container;

    public Collapsible(Element container) {
        this.container = container;
    }

    @Override
    public void onClicked(int mouseX, int mouseY, int button) {
        if (button == 1) container.getNormalStyle().setVisible(!container.getNormalStyle().isVisible());
    }
}
