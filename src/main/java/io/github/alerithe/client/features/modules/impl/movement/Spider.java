package io.github.alerithe.client.features.modules.impl.movement;

import io.github.alerithe.client.events.game.EventUpdate;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.utilities.EntityHelper;
import io.github.alerithe.events.impl.Subscribe;

public class Spider extends Module {
    public Spider() {
        super("Spider", new String[0], Type.MOVEMENT);
    }

    @Subscribe
    private void onPreUpdate(EventUpdate.Pre event) {
        // FIXME: How tired was I when I wrote this?
        if (!EntityHelper.getUser().isCollidedHorizontally) return;

        if (EntityHelper.getUser().fallDistance > 0) {
            EntityHelper.getUser().motionY = 0.2;
            EntityHelper.getUser().fallDistance = 0;
        }
    }
}
