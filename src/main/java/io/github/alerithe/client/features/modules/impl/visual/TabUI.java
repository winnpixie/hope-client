package io.github.alerithe.client.features.modules.impl.visual;

import io.github.alerithe.client.events.game.EventDraw;
import io.github.alerithe.client.events.game.EventInput;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.ui.tab.Container;
import io.github.alerithe.client.utilities.GameHelper;
import io.github.alerithe.events.impl.Subscribe;

public class TabUI extends Module {
    private Container container;

    public TabUI() {
        super("TabUI", new String[]{"tabgui"}, Type.VISUAL);
    }

    @Override
    public void onEnable() {
        if (container == null) container = new Container();
    }

    @Subscribe
    private void onOverlayDraw(EventDraw.Overlay event) {
        if (!GameHelper.getSettings().showDebugInfo) container.getRenderer().draw(event.getPartialTicks());
    }

    @Subscribe
    private void onKeyPress(EventInput.KeyPress event) {
        if (!GameHelper.getSettings().showDebugInfo) container.getController().onKeyPress(event.getKey());
    }
}
