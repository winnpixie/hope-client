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

            for (EventHandler handler : element.getHandlers()) {
                handler.onMouseDown(mouseX, mouseY, button);

                if (button == 0) {
                    handler.onLeftClick(mouseX, mouseY);
                } else if (button == 1) {
                    handler.onRightClick(mouseX, mouseY);
                } else if (button == 2) {
                    // Delegate unknowns to middle-click.
                    handler.onMiddleClick(mouseX, mouseY);
                }
            }
        } else {
            element.setFocused(false);
        }

        for (Element child : element.getChildren()) child.click(mouseX, mouseY, button);
    }

    public void release(int mouseX, int mouseY, int button) {
        if (!element.getNormalStyle().isVisible()) return;

        for (EventHandler handler : element.getHandlers()) handler.onMouseUp(mouseX, mouseY, button);

        for (Element child : element.getChildren()) child.release(mouseX, mouseY, button);
    }

    public void pressKey(char charCode, int keyCode) {
        if (!element.getNormalStyle().isVisible()) return;

        if (element.isFocused()) {
            for (EventHandler handler : element.getHandlers()) handler.onKeyPressed(charCode, keyCode);
        }

        for (Element child : element.getChildren()) child.pressKey(charCode, keyCode);
    }
}
