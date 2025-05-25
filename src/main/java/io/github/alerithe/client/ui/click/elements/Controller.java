package io.github.alerithe.client.ui.click.elements;

import io.github.alerithe.client.ui.click.elements.handlers.EventHandler;

public class Controller {
    private final Element element;

    public Controller(Element element) {
        this.element = element;
    }

    public void update() {
        if (!element.getNormalStyle().isVisible()) return;

        for (EventHandler handler : element.getHandlers()) handler.onUpdate();

        element.onUpdate();

        for (Element child : element.getChildren()) child.update();
    }

    public void click(int mouseX, int mouseY, int button) {
        if (!element.getNormalStyle().isVisible()) return;

        if (element.isInBounds(mouseX, mouseY)) {
            element.setFocused(true);

            for (EventHandler handler : element.getHandlers()) handler.onClicked(mouseX, mouseY, button);

            element.onClicked(mouseX, mouseY, button);
        } else {
            element.setFocused(false);
        }

        for (Element child : element.getChildren()) child.click(mouseX, mouseY, button);
    }

    public void release(int mouseX, int mouseY, int button) {
        if (!element.getNormalStyle().isVisible()) return;

        for (EventHandler handler : element.getHandlers()) handler.onRelease(mouseX, mouseY, button);

        element.onRelease(mouseX, mouseY, button);

        for (Element child : element.getChildren()) child.release(mouseX, mouseY, button);
    }

    public void pressKey(char charCode, int keyCode) {
        if (!element.getNormalStyle().isVisible()) return;

        if (element.isFocused()) {
            for (EventHandler handler : element.getHandlers()) handler.onKeyPressed(charCode, keyCode);

            element.onKeyPressed(charCode, keyCode);
        }

        for (Element child : element.getChildren()) child.pressKey(charCode, keyCode);
    }
}
