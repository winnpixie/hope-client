package io.github.alerithe.client.features.modules.impl.movement;

import io.github.alerithe.client.events.game.EventUpdate;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.utilities.EntityHelper;
import io.github.alerithe.events.impl.Subscribe;

public class FastLadder extends Module {
    public FastLadder() {
        super("FastLadder", new String[0], Type.MOVEMENT);
    }

    @Subscribe
    private void onPreUpdate(EventUpdate.Pre event) {
        // FIXME: Jesus, what was this value again?
        if (EntityHelper.getUser().isOnLadder()) EntityHelper.getUser().motionY = 0.42;
    }
}
