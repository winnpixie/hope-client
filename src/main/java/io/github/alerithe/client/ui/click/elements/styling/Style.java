package io.github.alerithe.client.ui.click.elements.styling;

public class Style<T extends Style<?>> {
    private T parent;
    private boolean visible = true;

    public Style() {
    }

    public Style(T parent) {
        setParent(parent);
    }

    public T getParent() {
        return parent;
    }

    public void setParent(T parent) {
        this.parent = parent;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
