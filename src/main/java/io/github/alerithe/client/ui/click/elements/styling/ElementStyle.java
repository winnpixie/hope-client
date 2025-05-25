package io.github.alerithe.client.ui.click.elements.styling;

import io.github.alerithe.client.ui.click.elements.styling.text.TextStyle;

public class ElementStyle extends Style<ElementStyle> {
    public TextStyle textStyle;
    public BorderStyle borderStyle;

    public ElementStyle(ElementStyle parent) {
        super(parent);
    }

    @Override
    public void setParent(ElementStyle parent) {
        super.setParent(parent);

        textStyle = new TextStyle(parent != null ? parent.textStyle : null);
        borderStyle = new BorderStyle(parent != null ? parent.borderStyle : null);
    }

    // BACKGROUND
    private boolean showBackground = true;
    private boolean inheritsShowBackground = true;

    private int backgroundColor = 0xFFFFFFFF;
    private boolean inheritsBackgroundColor = true;

    private int foregroundColor = 0xFF000000;
    private boolean inheritsForegroundColor = true;

    public boolean isShowBackground() {
        return inheritsShowBackground && getParent() != null
                ? getParent().isShowBackground() : showBackground;
    }

    public void setShowBackground(boolean showBackground) {
        this.showBackground = showBackground;

        setInheritsShowBackground(false);
    }

    public boolean isInheritsShowBackground() {
        return inheritsShowBackground;
    }

    public void setInheritsShowBackground(boolean inheritsShowBackground) {
        this.inheritsShowBackground = inheritsShowBackground;
    }

    public int getBackgroundColor() {
        return inheritsBackgroundColor && getParent() != null
                ? getParent().getBackgroundColor() : backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;

        setInheritsBackgroundColor(false);
    }

    public boolean isInheritsBackgroundColor() {
        return inheritsBackgroundColor;
    }

    public void setInheritsBackgroundColor(boolean inheritsBackgroundColor) {
        this.inheritsBackgroundColor = inheritsBackgroundColor;
    }

    public int getForegroundColor() {
        return inheritsForegroundColor && getParent() != null
                ? getParent().getForegroundColor() : foregroundColor;
    }

    public void setForegroundColor(int foregroundColor) {
        this.foregroundColor = foregroundColor;

        setInheritsForegroundColor(false);
    }

    public boolean isInheritsForegroundColor() {
        return inheritsForegroundColor;
    }

    public void setInheritsForegroundColor(boolean inheritsForegroundColor) {
        this.inheritsForegroundColor = inheritsForegroundColor;
    }
}
