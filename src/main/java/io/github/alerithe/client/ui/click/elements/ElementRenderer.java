package io.github.alerithe.client.ui.click.elements;

import io.github.alerithe.client.ui.click.elements.handlers.ElementEventListener;
import io.github.alerithe.client.ui.click.elements.styling.BorderStyle;
import io.github.alerithe.client.ui.click.elements.styling.ElementStyle;
import io.github.alerithe.client.ui.click.elements.styling.text.TextAlignment;
import io.github.alerithe.client.ui.click.elements.styling.text.TextPosition;
import io.github.alerithe.client.ui.click.elements.styling.text.TextStyle;
import io.github.alerithe.client.ui.click.elements.styling.text.WordWrapping;
import io.github.alerithe.client.utilities.graphics.VisualHelper;
import net.minecraft.client.renderer.GlStateManager;

import java.util.Collections;
import java.util.List;

public class ElementRenderer {
    private final Element element;

    public ElementRenderer(Element element) {
        this.element = element;
    }

    public void draw(int mouseX, int mouseY, float partialTicks) {
        if (!element.getNormalStyle().isVisible()) return;

        for (ElementEventListener handler : element.getEventListeners()) handler.onDraw(mouseX, mouseY, partialTicks);

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

        float elementX = element.getX();
        float elementY = element.getY();
        float elementWidth = element.getWidth();
        float elementHeight = element.getHeight();

        VisualHelper.GFX.drawSquare(elementX, elementY, elementWidth, elementHeight, style.getBackgroundColor());
    }

    private void drawBorders(BorderStyle style) {
        float elementX = element.getX();
        float elementY = element.getY();
        float elementWidth = element.getWidth();
        float elementHeight = element.getHeight();

        float borderTop = style.getTop();
        float borderBottom = style.getBottom();
        float borderLeft = style.getLeft();
        float borderRight = style.getRight();

        if (borderTop > 0) {
            VisualHelper.GFX.drawSquare(elementX,
                    elementY - borderTop,
                    elementWidth + borderRight,
                    borderTop,
                    style.getColorTop());
        }

        if (borderBottom > 0) {
            VisualHelper.GFX.drawSquare(elementX - borderLeft,
                    elementY + elementHeight,
                    elementWidth + borderLeft,
                    borderTop,
                    style.getColorBottom());
        }

        if (borderLeft > 0) {
            VisualHelper.GFX.drawSquare(elementX - borderLeft,
                    elementY - borderTop,
                    borderLeft,
                    elementHeight + borderTop,
                    style.getColorLeft());
        }

        if (borderRight > 0) {
            VisualHelper.GFX.drawSquare(elementX + elementWidth,
                    elementY,
                    borderRight,
                    elementHeight + borderBottom,
                    style.getColorRight());
        }
    }

    private void drawText(TextStyle style) {
        String text = element.getText();
        if (text == null) return;
        if (text.isEmpty()) return;

        if (!style.isVisible()) return;

        float elementX = element.getX();
        float elementY = element.getY();
        float elementWidth = element.getWidth();
        float elementHeight = element.getHeight();

        TextAlignment alignment = style.getAlignment();
        TextPosition position = style.getPosition();

        if (style.isItalic()) text = "\247o" + text;
        if (style.isBold()) text = "\247l" + text;

        float textWidth = VisualHelper.TXT.getStringWidth(text);

        float xOffset = style.getOffsetX();
        if (alignment == TextAlignment.CENTER) {
            xOffset += (elementWidth / 2f) - (textWidth / 2f);
        } else if (alignment == TextAlignment.RIGHT) {
            xOffset = elementWidth - textWidth - xOffset;
        }

        float fontHeight = VisualHelper.TXT.getFontHeight();
        float yOffset = style.getOffsetY();
        if (position == TextPosition.MIDDLE) {
            yOffset += (elementHeight / 2f) - (fontHeight / 2f);
        } else if (position == TextPosition.BOTTOM) {
            yOffset = elementHeight - fontHeight - yOffset;
        }

        GlStateManager.pushMatrix();
        GlStateManager.translate(elementX + xOffset, elementY + yOffset, 0f);
        GlStateManager.scale(style.getScaleX(), style.getScaleY(), 1f);
        GlStateManager.translate(-(elementX + xOffset), -(elementY + yOffset), 0f);

        if (style.isTextWrap()) {
            List<String> lines = Collections.emptyList();
            WordWrapping wordWrap = style.getWordWrapping();
            if (wordWrap == WordWrapping.NORMAL) {
                lines = VisualHelper.TXT.wrapStringPerWord(text, elementWidth);
            } else if (wordWrap == WordWrapping.BREAK_WORD) {
                lines = VisualHelper.TXT.wrapStringPerCharacter(text, elementWidth);
            }

            if (position == TextPosition.BOTTOM) {
                yOffset -= (lines.size() - 1) * fontHeight;
            }

            float lineY = elementY + yOffset;
            for (String line : lines) {
                // Re-calculate x-offset and text-width per line
                xOffset = style.getOffsetX();
                textWidth = VisualHelper.TXT.getStringWidth(line);

                if (alignment == TextAlignment.CENTER) {
                    xOffset += (elementWidth / 2f) - (textWidth / 2f);
                } else if (alignment == TextAlignment.RIGHT) {
                    xOffset = elementWidth - textWidth - xOffset;
                }

                VisualHelper.TXT.drawString(line,
                        elementX + xOffset,
                        lineY,
                        style.getColor(),
                        style.isShadow());
                lineY += fontHeight;
            }
        } else {
            VisualHelper.TXT.drawString(text,
                    elementX + xOffset,
                    elementY + yOffset,
                    style.getColor(),
                    style.isShadow());
        }

        GlStateManager.popMatrix();
    }
}