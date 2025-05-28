package io.github.alerithe.client.features.modules.impl.miscellaneous;

import io.github.alerithe.client.events.game.EventUpdate;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.events.impl.Subscribe;

public class NoHunger extends Module {
    public NoHunger() {
        super("NoHunger", new String[0], Type.MISCELLANEOUS);
    }

    @Subscribe
    private void onPreUpdate(EventUpdate.Pre event) {
        event.setOnGround(false);
    }
}
