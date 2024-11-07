package io.github.alerithe.client.ui.click.elements;

import io.github.alerithe.client.utilities.MathHelper;
import io.github.alerithe.client.utilities.VisualHelper;
import io.github.alerithe.client.utilities.Wrapper;

import java.util.ArrayList;
import java.util.List;

public class Element {
    private String text;
    private int x;
    private int y;
    private int width;
    private int height;
    private int textColor;
    private int backgroundColor;
    private final List<Element> children;

    public Element(String text, int x, int y, int width, int height) {
        this.text = text;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.textColor = 0xFFFFFFFF;
        this.backgroundColor = 0xFF990000;
        this.children = new ArrayList<>();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getMaxWidth() {
        int minX = x;
        int maxX = x + width;

        for (Element child : children) {
            minX = MathHelper.min(minX, child.x);
            maxX = MathHelper.max(maxX, child.x + child.width);
        }

        if (minX > maxX) {
            minX += maxX;
            maxX = minX - maxX;
            minX -= maxX;
        }

        return maxX - minX;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getMaxHeight() {
        int minY = y;
        int maxY = y + height;

        for (Element child : children) {
            minY = MathHelper.min(minY, child.y);
            maxY = MathHelper.max(maxY, child.y + child.height);
        }

        if (minY > maxY) {
            minY += maxY;
            maxY = minY - maxY;
            minY -= maxY;
        }

        return maxY - minY;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public List<Element> getChildren() {
        return children;
    }

    public boolean isInBounds(int x, int y) {
        return x >= this.x && y >= this.y && x < (this.x + this.width) && y < (this.y + this.height);
    }

    public void draw(int mouseX, int mouseY, float partialTicks) {
        VisualHelper.drawRect(x, y, x + width, y + height, backgroundColor);
        Wrapper.getTextRenderer().drawString(text,
                x + (width / 2f) - Wrapper.getTextRenderer().getStringWidth(text) / 2f,
                y + (height / 2f) - Wrapper.getTextRenderer().FONT_HEIGHT / 2f, textColor);
    }

    public void update() {
        for (Element element : children) {
            element.update();
        }
    }

    public void onClick(int mouseX, int mouseY, int button) {
    }

    public void onRelease(int mouseX, int mouseY, int button) {
    }
}
