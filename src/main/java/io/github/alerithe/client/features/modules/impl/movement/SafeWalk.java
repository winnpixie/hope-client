package io.github.alerithe.client.features.modules.impl.movement;

import io.github.alerithe.client.events.game.EventBlockEdgeTest;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.events.impl.Subscribe;

public class SafeWalk extends Module {
    public SafeWalk() {
        super("SafeWalk", new String[0], Type.MOVEMENT);
    }

    @Subscribe
    private void onBlockEdge(EventBlockEdgeTest event) {
        event.cancel();
    }
}
