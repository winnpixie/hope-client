package io.github.alerithe.client.features.modules.impl.movement;

import io.github.alerithe.client.events.bus.Subscribe;
import io.github.alerithe.client.events.game.EventUpdate;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.utilities.EntityHelper;

public class Spider extends Module {
    public Spider() {
        super("Spider", new String[0], Type.MOVEMENT);
    }

    @Subscribe
    public void onPreUpdate(EventUpdate.Pre event) {
        // FIXME: Try to find original motion value?
        if (!EntityHelper.getUser().isCollidedHorizontally) return;

        if (EntityHelper.getUser().fallDistance > 0) {
            EntityHelper.getUser().motionY = 1.0 / 32.0;
            EntityHelper.getUser().fallDistance = 0;
        }
    }
}
