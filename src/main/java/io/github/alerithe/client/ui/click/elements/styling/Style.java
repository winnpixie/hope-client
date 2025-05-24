package io.github.alerithe.client.ui.click.elements.styling;

public class Style {
    public boolean absolutePosition = false;

    public int backgroundColor = 0xFFFFFFFF;
    public int foregroundColor = 0xFF000000;

    public int borderTop = 0;
    public int borderBottom = 0;
    public int borderLeft = 0;
    public int borderRight = 0;
    public int borderColorTop = 0x00000000;
    public int borderColorBottom = 0x00000000;
    public int borderColorLeft = 0x00000000;
    public int borderColorRight = 0x00000000;

    public void setBorderSize(int size) {
        this.borderTop = size;
        this.borderBottom = size;
        this.borderLeft = size;
        this.borderRight = size;
    }

    public void setBorderColor(int color) {
        this.borderColorTop = color;
        this.borderColorBottom = color;
        this.borderColorLeft = color;
        this.borderColorRight = color;
    }

    public int textColor = 0xFF000000;
    public int textXOffset = 0;
    public int textYOffset = 0;
    public boolean textShadow = false;
    public boolean textItalic = false;
    public boolean textBold = false;
    public TextAlignment textAlignment = TextAlignment.LEFT;
    public TextPosition textPosition = TextPosition.TOP;
}
