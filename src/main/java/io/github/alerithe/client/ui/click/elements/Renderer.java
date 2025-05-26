package io.github.alerithe.client.ui.click.elements;

import io.github.alerithe.client.ui.click.elements.handlers.EventHandler;
import io.github.alerithe.client.ui.click.elements.styling.BorderStyle;
import io.github.alerithe.client.ui.click.elements.styling.ElementStyle;
import io.github.alerithe.client.ui.click.elements.styling.text.TextAlignment;
import io.github.alerithe.client.ui.click.elements.styling.text.TextPosition;
import io.github.alerithe.client.ui.click.elements.styling.text.TextStyle;
import io.github.alerithe.client.utilities.Wrapper;
import io.github.alerithe.client.utilities.graphics.VisualHelper;
import org.lwjgl.opengl.GL11;

public class Renderer {
    private final Element element;

    public Renderer(Element element) {
        this.element = element;
    }

    public void draw(int mouseX, int mouseY, float partialTicks) {
        if (!element.getNormalStyle().isVisible()) return;

        for (EventHandler handler : element.getHandlers()) handler.onDraw(mouseX, mouseY, partialTicks);

        element.setHovered(element.isInBounds(mouseX, mouseY));
        ElementStyle activeStyle = element.isHovered()
                ? element.getHoveredStyle() : element.getNormalStyle();

        drawBackground(activeStyle);
        drawBorders(activeStyle.borderStyle);
        drawText(activeStyle.textStyle);

        element.onDraw(mouseX, mouseY, partialTicks);

        for (Element child : element.getChildren()) child.draw(mouseX, mouseY, partialTicks);
    }

    private void drawBackground(ElementStyle style) {
        if (!style.isShowBackground()) return;

        float eX = element.getX();
        float eY = element.getY();
        float eWidth = element.getWidth();
        float eHeight = element.getHeight();

        VisualHelper.MC_GFX.drawSquare(eX, eY, eWidth, eHeight, style.getBackgroundColor());
    }

    private void drawBorders(BorderStyle style) {
        float eX = element.getX();
        float eY = element.getY();
        float eWidth = element.getWidth();
        float eHeight = element.getHeight();

        float borderTop = style.getTop();
        float borderBottom = style.getBottom();
        float borderLeft = style.getLeft();
        float borderRight = style.getRight();

        if (borderTop > 0) {
            VisualHelper.MC_GFX.drawSquare(eX,
                    eY - borderTop,
                    eWidth + borderRight,
                    borderTop,
                    style.getColorTop());
        }

        if (borderBottom > 0) {
            VisualHelper.MC_GFX.drawSquare(eX - borderLeft,
                    eY + eHeight,
                    eWidth + borderLeft,
                    borderTop,
                    style.getColorBottom());
        }

        if (borderLeft > 0) {
            VisualHelper.MC_GFX.drawSquare(eX - borderLeft,
                    eY - borderTop,
                    borderLeft,
                    eHeight + borderTop,
                    style.getColorLeft());
        }

        if (borderRight > 0) {
            VisualHelper.MC_GFX.drawSquare(eX + eWidth,
                    eY,
                    borderRight,
                    eHeight + borderBottom,
                    style.getColorBottom());
        }
    }

    private void drawText(TextStyle style) {
        String text = element.getText();
        if (text == null) return;
        if (text.isEmpty()) return;

        if (!style.isVisible()) return;

        float eX = element.getX();
        float eY = element.getY();
        float eWidth = element.getWidth();
        float eHeight = element.getHeight();

        TextAlignment alignment = style.getAlignment();
        TextPosition position = style.getPosition();

        if (style.isItalic()) text = "\247o" + text;
        if (style.isBold()) text = "\247l" + text;

        float textWidth = 0;
        if (alignment != TextAlignment.LEFT) {
            textWidth = VisualHelper.MC_FONT.getStringWidth(text);
        }

        float xOffset = style.getOffsetX();
        if (alignment == TextAlignment.CENTER) {
            xOffset += (eWidth / 2f) - (textWidth / 2f);
        } else if (alignment == TextAlignment.RIGHT) {
            xOffset = eWidth - textWidth - xOffset;
        }

        float fontHeight = VisualHelper.MC_FONT.getFontHeight();
        float yOffset = style.getOffsetY();
        if (position == TextPosition.MIDDLE) {
            yOffset += (eHeight / 2f) - (fontHeight / 2f);
        } else if (position == TextPosition.BOTTOM) {
            yOffset = eHeight - fontHeight - yOffset;
        }

        GL11.glPushMatrix();
        GL11.glTranslatef(eX + xOffset, eY + yOffset, 0f);
        GL11.glScalef(style.getScaleX(), style.getScaleY(), 1f);
        GL11.glTranslatef(-(eX + xOffset), -(eY + yOffset), 0f);

        if (style.isLineWrap()) {
            // FIXME: Develop own way to handle this instead of lazily depending on Minecraft's built-in way.
            Wrapper.getGame().fontRenderer.drawSplitString(text,
                    eX + xOffset,
                    eY + yOffset,
                    eWidth,
                    style.getColor(),
                    style.isShadow());
        } else {
            VisualHelper.MC_FONT.drawString(text,
                    eX + xOffset,
                    eY + yOffset,
                    style.getColor(),
                    style.isShadow());
        }

        GL11.glPopMatrix();
    }
}