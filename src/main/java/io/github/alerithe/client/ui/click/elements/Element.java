package io.github.alerithe.client.ui.click.elements;

import io.github.alerithe.client.ui.click.elements.styling.Style;
import io.github.alerithe.client.utilities.MathHelper;

import java.util.ArrayList;
import java.util.List;

public class Element {
    private Element parent;
    private int x;
    private int y;
    private int width;
    private int height;
    private String text;
    private Style style;
    private final List<Element> children;

    private final Renderer renderer = new Renderer(this);
    private final Controller controller = new Controller(this);

    public Element(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.style = new Style();
        this.children = new ArrayList<>();
    }

    public Element getParent() {
        return parent;
    }

    public void setParent(Element parent) {
        this.parent = parent;
    }

    public int getX() {
        if (parent == null || getStyle().absolutePosition) return x;

        return parent.getX() + x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        if (parent == null || getStyle().absolutePosition) return y;

        return parent.getY() + y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public int getTotalWidth() {
        int minX = getX();
        int maxX = getX() + getWidth();

        if (getController().expanded) {
            for (Element child : children) {
                if (child.getStyle().absolutePosition) continue;

                minX = MathHelper.min(minX, child.getX());
                maxX = MathHelper.max(maxX, child.getX() + child.getTotalWidth());
            }
        }

        if (minX > maxX) {
            int swp = maxX;
            maxX = minX;
            minX = swp;
        }

        return maxX - minX;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public int getTotalHeight() {
        int minY = getY();
        int maxY = getY() + getHeight();

        if (getController().expanded) {
            for (Element child : children) {
                if (child.getStyle().absolutePosition) continue;

                minY = MathHelper.min(minY, child.getY());
                maxY = MathHelper.max(maxY, child.getY() + child.getTotalHeight());
            }
        }

        if (minY > maxY) {
            int swp = maxY;
            maxY = minY;
            minY = swp;
        }

        return maxY - minY;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Style getStyle() {
        return style;
    }

    public void setStyle(Style style) {
        this.style = style;
    }

    public void addChild(Element child) {
        child.parent = this;

        children.add(child);
    }

    public List<Element> getChildren() {
        return children;
    }

    public Renderer getRenderer() {
        return renderer;
    }

    public Controller getController() {
        return controller;
    }

    public boolean isInBounds(int x, int y) {
        return x >= this.getX()
                && y >= this.getY()
                && x < (this.getX() + this.getWidth())
                && y < (this.getY() + this.getHeight());
    }

    public void draw(int mouseX, int mouseY, float partialTicks) {
        renderer.draw(mouseX, mouseY, partialTicks);
    }

    protected void drawChildren(int mouseX, int mouseY, float partialTicks) {
        for (Element child : children) child.draw(mouseX, mouseY, partialTicks);
    }

    public void update() {
        controller.update();
    }

    protected void updateChildren() {
        for (Element child : children) child.update();
    }

    public void click(int mouseX, int mouseY, int button) {
        controller.click(mouseX, mouseY, button);
    }

    protected void clickChildren(int mouseX, int mouseY, int button) {
        for (Element child : children) child.click(mouseX, mouseY, button);
    }

    public void release(int mouseX, int mouseY, int button) {
        controller.release(mouseX, mouseY, button);
    }

    protected void releaseChildren(int mouseX, int mouseY, int button) {
        for (Element child : children) child.release(mouseX, mouseY, button);
    }

    public void onDraw(int mouseX, int mouseY, float partialTicks) {
    }

    public void onUpdate() {
    }

    public void onClick(int mouseX, int mouseY, int button) {
    }

    public void onRelease(int mouseX, int mouseY, int button) {
    }
}
