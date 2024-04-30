package io.github.alerithe.client.features.modules.impl.visual;

import io.github.alerithe.client.events.EventDraw;
import io.github.alerithe.client.events.EventInput;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.ui.tab.Container;
import io.github.alerithe.client.utilities.Wrapper;
import io.github.alerithe.events.CallOrder;
import io.github.alerithe.events.Register;

public class TabUI extends Module {
    private Container container;

    public TabUI() {
        super("TabUI", new String[]{"tabgui"}, Type.VISUAL);
    }

    @Override
    public void enable() {
        if (container == null) container = new Container();

        super.enable();
    }

    @Register(CallOrder.UNIMPORTANT)
    private void onOverlayDraw(EventDraw.Overlay event) {
        if (!Wrapper.getGameSettings().showDebugInfo) container.getRenderer().draw(event.getPartialTicks());
    }

    @Register
    private void onKeyPress(EventInput.KeyPress event) {
        if (!Wrapper.getGameSettings().showDebugInfo) container.getController().onKeyPress(event.getKey());
    }
}
