package io.github.alerithe.client.features.modules.impl.movement;

import io.github.alerithe.events.Register;
import io.github.alerithe.client.events.EventBlockEdgeCheck;
import io.github.alerithe.client.features.modules.Module;

public class SafeWalk extends Module {
    public SafeWalk() {
        super("SafeWalk", new String[0], Type.MOVEMENT);
    }

    @Register
    private void onBlockEdge(EventBlockEdgeCheck event) {
        event.setCancelled(true);
    }
}
