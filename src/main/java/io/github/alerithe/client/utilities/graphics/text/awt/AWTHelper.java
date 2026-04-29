package io.github.alerithe.client.utilities.graphics.text.awt;

import java.awt.*;
import java.awt.image.BufferedImage;

public class AWTHelper {
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
