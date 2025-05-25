package io.github.alerithe.client.ui.click.elements.handlers;

import io.github.alerithe.client.ui.click.elements.Element;

public class EventHandler {
    private Element source;

    public Element getSource() {
        return source;
    }

    public void setSource(Element source) {
        this.source = source;
    }

    public void onUpdate() {
    }

    public void onDraw(int mouseX, int mouseY, float partialTicks) {
    }

    public void onClicked(int mouseX, int mouseY, int button) {
    }

    public void onRelease(int mouseX, int mouseY, int button) {
    }

    public void onKeyPressed(char charCode, int keyCode) {
    }
}
