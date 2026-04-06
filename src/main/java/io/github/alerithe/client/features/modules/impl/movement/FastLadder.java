package io.github.alerithe.client.features.modules.impl.movement;

import io.github.alerithe.client.events.bus.Subscribe;
import io.github.alerithe.client.events.game.EventUpdate;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.utilities.EntityHelper;

public class FastLadder extends Module {
    public FastLadder() {
        super("FastLadder", new String[0], Type.MOVEMENT);
    }

    @Subscribe
    private void onPreUpdate(EventUpdate.Pre event) {
        if (EntityHelper.getUser().isOnLadder()) EntityHelper.getUser().motionY = 0.42;
    }
}
