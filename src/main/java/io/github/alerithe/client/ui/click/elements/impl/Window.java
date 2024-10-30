package io.github.alerithe.client.ui.click.elements.impl;

import io.github.alerithe.client.ui.click.elements.Element;
import io.github.alerithe.client.utilities.MathHelper;
import io.github.alerithe.client.utilities.VisualHelper;
import net.minecraft.client.gui.ScaledResolution;

public class Window extends Element {
    private boolean open;
    private boolean dragging;
    private int offsetX;
    private int offsetY;

    public Window(String text, int x, int y, int width, int height) {
        super(text, x, y, width, height);
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public boolean isDragging() {
        return dragging;
    }

    @Override
    public void onClick(int mouseX, int mouseY, int button) {
        if (isInBounds(mouseX, mouseY)) {
            if (button == 0) {
                offsetX = getX() - mouseX;
                offsetY = getY() - mouseY;
                dragging = true;
            } else if (button == 1) {
                open = !open;
            }
        }

        if (open) {
            for (Element element : getChildren()) {
                element.onClick(mouseX, mouseY, button);
            }
        }
    }

    @Override
    public void onRelease(int mouseX, int mouseY, int button) {
        if (button == 0) dragging = false;
    }

    @Override
    public int getMaxHeight() {
        if (!open) {
            int minY = getY();
            int maxY = getY() + getHeight();

            if (minY > maxY) {
                minY += maxY;
                maxY = minY - maxY;
                minY -= maxY;
            }

            return maxY - minY;
        }

        return super.getMaxHeight();
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        if (dragging) {
            ScaledResolution display = VisualHelper.getDisplay();
            int x = mouseX + offsetX;
            int y = mouseY + offsetY;

            setX(MathHelper.clamp(x, 1, display.getScaledWidth() - getWidth() - 1));
            setY(MathHelper.clamp(y, 1, display.getScaledHeight() - getMaxHeight() - 1));
        }

        int offset = getHeight();
        for (Element child : getChildren()) {
            int widthDiff = getWidth() - child.getWidth();
            child.setX(getX() + (widthDiff / 2));
            child.setY(getY() + offset);
            offset += child.getHeight();
        }

        super.draw(mouseX, mouseY, partialTicks);
        if (open) {
            for (Element element : getChildren()) {
                element.draw(mouseX, mouseY, partialTicks);
            }
        }
    }
}
