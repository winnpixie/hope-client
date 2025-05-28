package io.github.alerithe.client.ui.click.elements.handlers.impl;

import io.github.alerithe.client.ui.click.elements.Element;
import io.github.alerithe.client.ui.click.elements.handlers.EventHandler;
import io.github.alerithe.client.utilities.graphics.VisualHelper;
import net.minecraft.client.gui.ScaledResolution;

public class Draggable extends EventHandler {
    private boolean dragging;

    public float initialX;
    public float initialY;

    @Override
    public void onDraw(int mouseX, int mouseY, float partialTicks) {
        if (!dragging) return;

        float newX = mouseX + initialX;
        float newY = mouseY + initialY;

        float minX = 1;
        float minY = 1;

        ScaledResolution display = VisualHelper.getDisplay();
        float maxX = display.getScaledWidth() - 1;
        float maxY = display.getScaledHeight() - 1;

        Element parent = getSource().getParent();
        if (parent != null) {
            minX = parent.getX() + 1;
            minY = parent.getY() + 1;

            maxX = parent.getX() + parent.getWidth() - 1;
            maxY = parent.getY() + parent.getHeight() - 1;
        }

        float deltaX = newX - getSource().getX();
        float deltaY = newY - getSource().getY();

        float[] bounds = getSource().getBounds();
        if (bounds[0] + deltaX < minX) {
            newX = minX;
        }

        if (bounds[1] + deltaY < minY) {
            newY = minY;
        }

        if (bounds[2] + deltaX > maxX) {
            newX = maxX - (bounds[2] - bounds[0]);
        }

        if (bounds[3] + deltaY > maxY) {
            newY = maxY - (bounds[3] - bounds[1]);
        }

        getSource().setX(newX);
        getSource().setY(newY);
    }

    @Override
    public void onMouseDown(int mouseX, int mouseY, int button) {
        if (button != 0) return;

        initialX = getSource().getX() - mouseX;
        initialY = getSource().getY() - mouseY;
        dragging = true;
    }

    @Override
    public void onMouseUp(int mouseX, int mouseY, int button) {
        if (button == 0) dragging = false;
    }
}
