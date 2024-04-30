package io.github.alerithe.client.features.modules.impl.movement;

import io.github.alerithe.client.events.EventUpdate;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.utilities.Wrapper;
import io.github.alerithe.events.Register;

public class FastLadder extends Module {
    public FastLadder() {
        super("FastLadder", new String[0], Type.MOVEMENT);
    }

    @Register
    private void onPreUpdate(EventUpdate.Pre event) {
        // FIXME: Jesus, what was this value again?
        if (Wrapper.getPlayer().isOnLadder()) Wrapper.getPlayer().motionY = 0.42;
    }
}
