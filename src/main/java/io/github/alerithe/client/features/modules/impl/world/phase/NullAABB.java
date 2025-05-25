package io.github.alerithe.client.features.modules.impl.world.phase;

import io.github.alerithe.client.events.game.EventBlockCollision;
import io.github.alerithe.client.utilities.Wrapper;

public class NullAABB extends PhaseMode {
    public NullAABB() {
        super("NullAABB", new String[]{"null"});
    }

    @Override
    public void onCollision(EventBlockCollision event) {
        if (event.getBoundingBox() == null) return;
        if (!Wrapper.getPlayer().isSneaking()) return;

        event.setBoundingBox(null); // To think, Lemons method still works years later.
    }
}
