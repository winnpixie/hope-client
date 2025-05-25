package io.github.alerithe.client.ui.click.elements.styling.text;

import io.github.alerithe.client.ui.click.elements.styling.Style;

public class TextStyle extends Style<TextStyle> {
    public TextStyle(TextStyle parent) {
        super(parent);
    }

    private int color = 0xFF000000;
    private boolean inheritsColor = true;

    private float offsetX = 0f;
    private boolean inheritsOffsetX = true;

    private float offsetY = 0f;
    private boolean inheritsOffsetY = true;

    private float scaleX = 1f;
    private boolean inheritsScaleX = true;

    private float scaleY = 1f;
    private boolean inheritsScaleY = true;

    private boolean shadow = false;
    private boolean inheritsShadow = true;

    private boolean italic = false;
    private boolean inheritsItalics = true;

    private boolean bold = false;
    private boolean inheritsBold = true;

    private TextAlignment alignment = TextAlignment.LEFT;
    private boolean inheritsAlignment = true;

    private TextPosition position = TextPosition.TOP;
    private boolean inheritsPosition = true;

    private boolean lineWrap = false;
    private boolean inheritsLineWrap = true;

    public int getColor() {
        return inheritsColor && getParent() != null
                ? getParent().getColor() : color;
    }

    public void setColor(int color) {
        this.color = color;

        setInheritsColor(false);
    }

    public boolean isInheritsColor() {
        return inheritsColor;
    }

    public void setInheritsColor(boolean inheritsColor) {
        this.inheritsColor = inheritsColor;
    }

    public float getOffsetX() {
        return inheritsOffsetX && getParent() != null
                ? getParent().getOffsetX() : offsetX;
    }

    public void setOffsetX(float offsetX) {
        this.offsetX = offsetX;

        setInheritsOffsetX(false);
    }

    public boolean isInheritsOffsetX() {
        return inheritsOffsetX;
    }

    public void setInheritsOffsetX(boolean inheritsOffsetX) {
        this.inheritsOffsetX = inheritsOffsetX;
    }

    public float getOffsetY() {
        return inheritsOffsetY && getParent() != null
                ? getParent().getOffsetY() : offsetY;
    }

    public void setOffsetY(float offsetY) {
        this.offsetY = offsetY;

        setInheritsOffsetY(false);
    }

    public boolean isInheritsOffsetY() {
        return inheritsOffsetY;
    }

    public void setInheritsOffsetY(boolean inheritsOffsetY) {
        this.inheritsOffsetY = inheritsOffsetY;
    }

    public float getScaleX() {
        return inheritsScaleX && getParent() != null
                ? getParent().getScaleX() : scaleX;
    }

    public void setScaleX(float scaleX) {
        this.scaleX = scaleX;

        setInheritsScaleX(false);
    }

    public boolean isInheritsScaleX() {
        return inheritsScaleX;
    }

    public void setInheritsScaleX(boolean inheritsScaleX) {
        this.inheritsScaleX = inheritsScaleX;
    }

    public float getScaleY() {
        return inheritsScaleY && getParent() != null
                ? getParent().getScaleY() : scaleY;
    }

    public void setScaleY(float scaleY) {
        this.scaleY = scaleY;

        setInheritsScaleY(false);
    }

    public boolean isInheritsScaleY() {
        return inheritsScaleY;
    }

    public void setInheritsScaleY(boolean inheritsScaleY) {
        this.inheritsScaleY = inheritsScaleY;
    }

    public boolean isShadow() {
        return inheritsShadow && getParent() != null
                ? getParent().isShadow() : shadow;
    }

    public void setShadow(boolean shadow) {
        this.shadow = shadow;

        setInheritsShadow(false);
    }

    public boolean isInheritsShadow() {
        return inheritsShadow;
    }

    public void setInheritsShadow(boolean inheritsShadow) {
        this.inheritsShadow = inheritsShadow;
    }

    public boolean isItalic() {
        return inheritsItalics && getParent() != null
                ? getParent().isItalic() : italic;
    }

    public void setItalic(boolean italic) {
        this.italic = italic;

        setInheritsItalics(false);
    }

    public boolean isInheritsItalics() {
        return inheritsItalics;
    }

    public void setInheritsItalics(boolean inheritsItalics) {
        this.inheritsItalics = inheritsItalics;
    }

    public boolean isBold() {
        return inheritsBold && getParent() != null
                ? getParent().isBold() : bold;
    }

    public void setBold(boolean bold) {
        this.bold = bold;

        setInheritsBold(false);
    }

    public boolean isInheritsBold() {
        return inheritsBold;
    }

    public void setInheritsBold(boolean inheritsBold) {
        this.inheritsBold = inheritsBold;
    }

    public TextAlignment getAlignment() {
        return inheritsAlignment && getParent() != null
                ? getParent().getAlignment() : alignment;
    }

    public void setAlignment(TextAlignment alignment) {
        this.alignment = alignment;

        setInheritsAlignment(false);
    }

    public boolean isInheritsAlignment() {
        return inheritsAlignment;
    }

    public void setInheritsAlignment(boolean inheritsAlignment) {
        this.inheritsAlignment = inheritsAlignment;
    }

    public TextPosition getPosition() {
        return inheritsPosition && getParent() != null
                ? getParent().getPosition() : position;
    }

    public void setPosition(TextPosition position) {
        this.position = position;

        setInheritsPosition(false);
    }

    public boolean isInheritsPosition() {
        return inheritsPosition;
    }

    public void setInheritsPosition(boolean inheritsPosition) {
        this.inheritsPosition = inheritsPosition;
    }

    public boolean isLineWrap() {
        return inheritsLineWrap && getParent() != null
                ? getParent().isLineWrap() : lineWrap;
    }

    public void setLineWrap(boolean lineWrap) {
        this.lineWrap = lineWrap;

        setInheritsLineWrap(false);
    }

    public boolean isInheritsLineWrap() {
        return inheritsLineWrap;
    }

    public void setInheritsLineWrap(boolean inheritsLineWrap) {
        this.inheritsLineWrap = inheritsLineWrap;
    }
}
