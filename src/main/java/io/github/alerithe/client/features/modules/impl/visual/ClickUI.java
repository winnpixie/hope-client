package io.github.alerithe.client.features.modules.impl.visual;

import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.features.properties.impl.BooleanProperty;
import io.github.alerithe.client.ui.click.FreeFormUI;
import io.github.alerithe.client.ui.click.WindowedUI;
import io.github.alerithe.client.utilities.Wrapper;
import net.minecraft.client.gui.GuiScreen;

public class ClickUI extends Module {
    private final BooleanProperty newUI = new BooleanProperty("NewUI", new String[0], true);

    private GuiScreen controller;

    public ClickUI() {
        super("ClickUI", new String[]{"clickgui"}, Type.VISUAL);

        getPropertyManager().add(newUI);
    }

    @Override
    public void enable() {
        if (controller instanceof FreeFormUI) {
            if (newUI.getValue()) controller = new WindowedUI();
        } else if (controller instanceof WindowedUI) {
            if (!newUI.getValue()) controller = new FreeFormUI();
        } else {
            controller = newUI.getValue() ? new WindowedUI() : new FreeFormUI();
        }

        Wrapper.getGame().displayGuiScreen(controller);
        toggle();
    }
}
