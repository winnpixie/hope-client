package io.github.alerithe.client.ui.click.elements;

import io.github.alerithe.client.ui.click.elements.handlers.ElementEventListener;
import io.github.alerithe.client.ui.click.elements.styling.ElementStyle;
import io.github.alerithe.client.utilities.MathHelper;

import java.util.ArrayList;
import java.util.List;

public class Element {
    private Element parent;

    private float x;
    private float y;
    private float width;
    private float height;
    private final float[] bounds = new float[4];
    private String text;
    private final List<ElementEventListener> eventListeners = new ArrayList<>();
    private final List<Element> children = new ArrayList<>();

    private boolean hovered;
    private boolean focused;

    private ElementStyle normalStyle = new ElementStyle(null);
    private ElementStyle hoveredStyle = new ElementStyle(normalStyle);

    private final ElementRenderer renderer = new ElementRenderer(this);
    private final ElementController controller = new ElementController(this);

    public Element(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public Element getParent() {
        return parent;
    }

    public void setParent(Element parent) {
        if (parent == this) throw new IllegalArgumentException("An Element can not adopt itself!");

        this.parent = parent;
    }

    public float getX() {
        if (this.parent == null) return x;

        return parent.getX() + x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        if (this.parent == null) return y;

        return parent.getY() + y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getMinX() {
        return getBounds()[0];
    }

    public float getMaxX() {
        return getBounds()[2];
    }

    public float getMinY() {
        return getBounds()[1];
    }

    public float getMaxY() {
        return getBounds()[3];
    }

    public float[] getBounds() {
        bounds[0] = getX();
        bounds[1] = getY();
        bounds[2] = getX() + getWidth();
        bounds[3] = getY() + getHeight();

        for (Element child : children) {
            if (!child.getNormalStyle().isVisible()) continue;

            float[] childBounds = child.getBounds();
            bounds[0] = MathHelper.min(bounds[0], childBounds[0]);
            bounds[1] = MathHelper.min(bounds[1], childBounds[1]);
            bounds[2] = MathHelper.max(bounds[2], childBounds[2]);
            bounds[3] = MathHelper.max(bounds[3], childBounds[3]);
        }

        return bounds;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ElementStyle getNormalStyle() {
        return normalStyle;
    }

    public void setNormalStyle(ElementStyle normalStyle) {
        this.normalStyle = normalStyle;

        hoveredStyle.setParent(normalStyle);
    }

    public ElementStyle getHoveredStyle() {
        return hoveredStyle;
    }

    public void setHoveredStyle(ElementStyle hoveredStyle) {
        this.hoveredStyle = hoveredStyle;
    }

    public void addListener(ElementEventListener listener) {
        listener.setSource(this);
        eventListeners.add(listener);
    }

    public void addListeners(ElementEventListener... listeners) {
        for (ElementEventListener listener : listeners) {
            addListener(listener);
        }
    }

    public void removeListener(ElementEventListener listener) {
        eventListeners.remove(listener);
    }

    // TODO: Make this return an immutable copy?
    public List<ElementEventListener> getEventListeners() {
        return eventListeners;
    }

    public void addChild(Element child) {
        if (child.getParent() != null) child.getParent().removeChild(child);

        child.setParent(this);
        children.add(child);
    }

    public void addChildren(Element... children) {
        for (Element child : children) addChild(child);
    }

    public void removeChild(Element child) {
        child.setParent(null);
        children.remove(child);
    }

    public void removeChildren(Element... children) {
        for (Element child : children) removeChild(child);
    }

    public void clearChildren() {
        for (int i = children.size(); i > 0; i--) {
            Element child = children.get(i - 1);
            child.clearChildren();

            child.remove();
        }
    }

    // TODO: Make this return an immutable copy?
    public List<Element> getChildren() {
        return children;
    }

    public void remove() {
        if (parent != null) parent.removeChild(this);
    }

    public boolean isHovered() {
        return hovered;
    }

    public void setHovered(boolean hovered) {
        this.hovered = hovered;
    }

    public boolean isFocused() {
        return focused;
    }

    public void setFocused(boolean focused) {
        this.focused = focused;
    }

    public ElementRenderer getRenderer() {
        return renderer;
    }

    public ElementController getController() {
        return controller;
    }

    public boolean isInBounds(int x, int y) {
        return x >= getX() && y >= getY()
                && x < (getX() + getWidth()) && y < (getY() + getHeight());
    }

    public void draw(int mouseX, int mouseY, float partialTicks) {
        renderer.draw(mouseX, mouseY, partialTicks);
    }

    public void update() {
        controller.update();
    }

    public void click(int mouseX, int mouseY, int button) {
        controller.click(mouseX, mouseY, button);
    }

    public void release(int mouseX, int mouseY, int button) {
        controller.release(mouseX, mouseY, button);
    }

    public void pressKey(char charCode, int keyCode) {
        controller.pressKey(charCode, keyCode);
    }

    public void onDraw(int mouseX, int mouseY, float partialTicks) {
    }

    public void onUpdate() {
    }
}
