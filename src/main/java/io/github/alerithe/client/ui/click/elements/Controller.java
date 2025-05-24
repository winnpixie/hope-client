package io.github.alerithe.client.ui.click.elements;

public class Controller {
    private final Element element;

    public boolean closeable;
    public boolean expanded = true;

    public boolean draggable;
    public boolean dragging;

    public int dragX;
    public int dragY;

    public Controller(Element element) {
        this.element = element;
    }

    public void update() {
        element.onUpdate();
        if (expanded) element.updateChildren();
    }

    public void click(int mouseX, int mouseY, int button) {
        if (element.isInBounds(mouseX, mouseY)) {
            if (draggable && button == 0) {
                dragX = element.getX() - mouseX;
                dragY = element.getY() - mouseY;
                dragging = true;
            }

            if (closeable && button == 1) {
                expanded = !expanded;
            }

            element.onClick(mouseX, mouseY, button);
        }

        if (expanded) element.clickChildren(mouseX, mouseY, button);
    }

    public void release(int mouseX, int mouseY, int button) {
        if (button == 0) dragging = false;

        element.onRelease(mouseX, mouseY, button);
        if (expanded) element.releaseChildren(mouseX, mouseY, button);
    }
}
