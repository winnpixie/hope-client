package io.github.alerithe.client.ui.click.elements;

import io.github.alerithe.client.ui.click.elements.styling.Style;
import io.github.alerithe.client.ui.click.elements.styling.TextAlignment;
import io.github.alerithe.client.ui.click.elements.styling.TextPosition;
import io.github.alerithe.client.utilities.MathHelper;
import io.github.alerithe.client.utilities.VisualHelper;
import io.github.alerithe.client.utilities.Wrapper;
import net.minecraft.client.gui.ScaledResolution;

public class Renderer {
    private final Element element;

    public boolean showBackground = true;
    public boolean showText = true;

    public Renderer(Element element) {
        this.element = element;
    }

    public void draw(int mouseX, int mouseY, float partialTicks) {
        processDragging(mouseX, mouseY);

        Style style = element.getStyle();
        int eX = element.getX();
        int eY = element.getY();
        int eWidth = element.getWidth();
        int eHeight = element.getHeight();

        if (showBackground) {
            VisualHelper.drawSquare(eX, eY, eWidth, eHeight, style.backgroundColor);

            if (style.borderTop > 0) {
                VisualHelper.drawRect(eX,
                        eY - style.borderTop,
                        eX + eWidth + style.borderRight,
                        eY,
                        style.borderColorTop);
            }

            if (style.borderBottom > 0) {
                VisualHelper.drawRect(eX - style.borderLeft,
                        eY + eHeight,
                        eX + eWidth,
                        eY + eHeight + style.borderBottom,
                        style.borderColorBottom);
            }

            if (style.borderLeft > 0) {
                VisualHelper.drawRect(eX - style.borderLeft,
                        eY - style.borderTop,
                        eX,
                        eY + eHeight,
                        style.borderColorLeft);
            }

            if (style.borderRight > 0) {
                VisualHelper.drawRect(eX + eWidth,
                        eY,
                        eX + eWidth + style.borderRight,
                        eY + eHeight + style.borderBottom,
                        style.borderColorRight);
            }
        }

        if (showText) {
            String text = element.getText();
            if (text != null && !text.isEmpty()) {
                if (style.textItalic) text = "\247o" + text;
                if (style.textBold) text = "\247l" + text;

                int textWidth = 0;
                if (style.textAlignment != TextAlignment.LEFT) {
                    textWidth = Wrapper.getTextRenderer().getStringWidth(text);
                }

                float textXOffset = style.textXOffset;
                if (style.textAlignment == TextAlignment.CENTER) {
                    textXOffset += (eWidth / 2f) - (textWidth / 2f);
                } else if (style.textAlignment == TextAlignment.RIGHT) {
                    textXOffset = eWidth - textWidth - textXOffset;
                }

                int fontHeight = Wrapper.getTextRenderer().FONT_HEIGHT;
                float textYOffset = style.textYOffset;
                if (style.textPosition == TextPosition.MIDDLE) {
                    textYOffset += (eHeight / 2f) - (fontHeight / 2f);
                } else if (style.textPosition == TextPosition.BOTTOM) {
                    textYOffset = eHeight - fontHeight - textYOffset;
                }

                Wrapper.getTextRenderer().drawString(text,
                        eX + textXOffset,
                        eY + textYOffset,
                        style.textColor,
                        style.textShadow);
            }
        }

        element.onDraw(mouseX, mouseY, partialTicks);

        if (element.getController().expanded) element.drawChildren(mouseX, mouseY, partialTicks);
    }

    private void processDragging(int mouseX, int mouseY) {
        Controller controller = element.getController();
        if (controller.dragging) {
            ScaledResolution display = VisualHelper.getDisplay();
            int newX = mouseX + controller.dragX;
            int newY = mouseY + controller.dragY;

            newX = MathHelper.clamp(newX, 1, display.getScaledWidth() - element.getTotalWidth() - 1);
            newY = MathHelper.clamp(newY, 1, display.getScaledHeight() - element.getTotalHeight() - 1);

            element.setX(newX);
            element.setY(newY);
        }
    }
}
