package io.github.alerithe.client.events;

import net.minecraft.client.gui.GuiScreen;

public class EventGuiOpen {
    private final GuiScreen screen;

    public EventGuiOpen(GuiScreen screen) {
        this.screen = screen;
    }

    public GuiScreen getScreen() {
        return screen;
    }
}
