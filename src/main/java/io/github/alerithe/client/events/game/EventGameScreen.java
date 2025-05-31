package io.github.alerithe.client.events.game;

import net.minecraft.client.gui.GuiScreen;

public class EventGameScreen {
    private final GuiScreen screen;

    public EventGameScreen(GuiScreen screen) {
        this.screen = screen;
    }

    public GuiScreen getScreen() {
        return screen;
    }

    public static class Open extends EventGameScreen {
        public Open(GuiScreen screen) {
            super(screen);
        }
    }

    public static class Close extends EventGameScreen {
        public Close(GuiScreen screen) {
            super(screen);
        }
    }
}
