package io.github.alerithe.client.ui.click.elements.styling;

public class BorderStyle extends Style<BorderStyle> {
    public BorderStyle(BorderStyle parent) {
        super(parent);
    }

    private float top = 0;
    private boolean inheritsTop = true;

    private float bottom = 0;
    private boolean inheritsBottom = true;

    private float left = 0;
    private boolean inheritsLeft = true;

    private float right = 0;
    private boolean inheritsRight = true;

    private int colorTop = 0x00000000;
    private boolean inheritsColorTop = true;

    private int colorBottom = 0x00000000;
    private boolean inheritsColorBottom = true;

    private int colorLeft = 0x00000000;
    private boolean inheritsColorLeft = true;

    public int colorRight = 0x00000000;
    private boolean inheritsColorRight = true;

    public float getTop() {
        return inheritsTop && getParent() != null
                ? getParent().getTop() : top;
    }

    public void setTop(float top) {
        this.top = top;

        setInheritsTop(false);
    }

    public boolean isInheritsTop() {
        return inheritsTop;
    }

    public void setInheritsTop(boolean inheritsTop) {
        this.inheritsTop = inheritsTop;
    }

    public float getBottom() {
        return inheritsBottom && getParent() != null
                ? getParent().getBottom() : bottom;
    }

    public void setBottom(float bottom) {
        this.bottom = bottom;

        setInheritsBottom(false);
    }

    public boolean isInheritsBottom() {
        return inheritsBottom;
    }

    public void setInheritsBottom(boolean inheritsBottom) {
        this.inheritsBottom = inheritsBottom;
    }

    public float getLeft() {
        return inheritsLeft && getParent() != null
                ? getParent().getLeft() : left;
    }

    public void setLeft(float left) {
        this.left = left;

        setInheritsLeft(false);
    }

    public boolean isInheritsLeft() {
        return inheritsLeft;
    }

    public void setInheritsLeft(boolean inheritsLeft) {
        this.inheritsLeft = inheritsLeft;
    }

    public float getRight() {
        return inheritsRight && getParent() != null
                ? getParent().getRight() : right;
    }

    public void setRight(float right) {
        this.right = right;

        setInheritsRight(false);
    }

    public boolean isInheritsRight() {
        return inheritsRight;
    }

    public void setInheritsRight(boolean inheritsRight) {
        this.inheritsRight = inheritsRight;
    }

    public int getColorTop() {
        return inheritsColorTop && getParent() != null
                ? getParent().getColorTop() : colorTop;
    }

    public void setColorTop(int colorTop) {
        this.colorTop = colorTop;

        setInheritsColorTop(false);
    }

    public boolean isInheritsColorTop() {
        return inheritsColorTop;
    }

    public void setInheritsColorTop(boolean inheritsColorTop) {
        this.inheritsColorTop = inheritsColorTop;
    }

    public int getColorBottom() {
        return inheritsColorBottom && getParent() != null
                ? getParent().getColorBottom() : colorBottom;
    }

    public void setColorBottom(int colorBottom) {
        this.colorBottom = colorBottom;

        setInheritsColorBottom(false);
    }

    public boolean isInheritsColorBottom() {
        return inheritsColorBottom;
    }

    public void setInheritsColorBottom(boolean inheritsColorBottom) {
        this.inheritsColorBottom = inheritsColorBottom;
    }

    public int getColorLeft() {
        return inheritsColorLeft && getParent() != null
                ? getParent().getColorLeft() : colorLeft;
    }

    public void setColorLeft(int colorLeft) {
        this.colorLeft = colorLeft;

        setInheritsColorLeft(false);
    }

    public boolean isInheritsColorLeft() {
        return inheritsColorLeft;
    }

    public void setInheritsColorLeft(boolean inheritsColorLeft) {
        this.inheritsColorLeft = inheritsColorLeft;
    }

    public int getColorRight() {
        return inheritsColorRight && getParent() != null
                ? getParent().getColorRight() : colorRight;
    }

    public void setColorRight(int colorRight) {
        this.colorRight = colorRight;

        setInheritsColorRight(false);
    }

    public boolean isInheritsColorRight() {
        return inheritsColorRight;
    }

    public void setInheritsColorRight(boolean inheritsColorRight) {
        this.inheritsColorRight = inheritsColorRight;
    }

    public void setBorderSize(float size) {
        setTop(size);
        setBottom(size);
        setLeft(size);
        setRight(size);
    }

    public void setBorderColor(int color) {
        setColorTop(color);
        setColorBottom(color);
        setColorLeft(color);
        setColorRight(color);
    }
}
