package io.github.alerithe.client.features.modules.impl.movement;

import io.github.alerithe.client.events.game.EventUpdate;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.utilities.Wrapper;
import io.github.alerithe.events.Register;

public class Spider extends Module {
    public Spider() {
        super("Spider", new String[0], Type.MOVEMENT);
    }

    @Register
    private void onPreUpdate(EventUpdate.Pre event) {
        // FIXME: How tired was I when I wrote this?
        if (!Wrapper.getPlayer().isCollidedHorizontally) return;

        if (Wrapper.getPlayer().fallDistance > 0) {
            Wrapper.getPlayer().motionY = 0.2;
            Wrapper.getPlayer().fallDistance = 0;
        }
    }
}
