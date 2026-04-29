package io.github.alerithe.client.utilities.graphics.text.awt;

import java.awt.*;
import java.awt.image.BufferedImage;

public class AWTHelper {
    static final Color BLACK = new Color(0xFF000000, true);
    static final Color DARK_BLUE = new Color(0xFF0000AA, true);
    static final Color DARK_GREEN = new Color(0xFF00AA00, true);
    static final Color DARK_AQUA = new Color(0xFF00AAAA, true);
    static final Color DARK_RED = new Color(0xFFAA0000, true);
    static final Color DARK_PURPLE = new Color(0xFFAA00AA, true);
    static final Color GOLD = new Color(0xFFFFAA00, true);
    static final Color GRAY = new Color(0xFFAAAAAA, true);
    static final Color DARK_GRAY = new Color(0xFF555555, true);
    static final Color BLUE = new Color(0xFF5555FF, true);
    static final Color GREEN = new Color(0xFF55FF55, true);
    static final Color CYAN = new Color(0xFF55FFFF, true);
    static final Color RED = new Color(0xFFFF5555, true);
    static final Color MAGENTA = new Color(0xFFFF55FF, true);
    static final Color YELLOW = new Color(0xFFFFFF55, true);
    static final Color WHITE = new Color(0xFFFFFFFF, true);

    static final GraphicsConfiguration DISPLAY;
    static final BufferedImage TEMPLATE;

    static {
        DISPLAY = GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getDefaultScreenDevice()
                .getDefaultConfiguration();

        TEMPLATE = DISPLAY.createCompatibleImage(1, 1, Transparency.OPAQUE);
    }

    private AWTHelper() {
    }
}
