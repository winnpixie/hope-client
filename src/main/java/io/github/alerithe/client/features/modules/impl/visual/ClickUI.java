package io.github.alerithe.client.features.modules.impl.visual;

import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.ui.click.GuiClick;
import io.github.alerithe.client.utilities.Wrapper;

public class ClickUI extends Module {
    private GuiClick controller;

    public ClickUI() {
        super("ClickUI", new String[]{"clickgui"}, Type.VISUAL);
    }

    @Override
    public void enable() {
        if (controller == null) controller = new GuiClick();

        Wrapper.getGame().displayGuiScreen(controller);
        toggle();
    }
}
