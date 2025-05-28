package io.github.alerithe.client.features.modules.impl.player;

import io.github.alerithe.client.events.game.EventUpdate;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.utilities.EntityHelper;
import io.github.alerithe.events.impl.Subscribe;

public class AutoRespawn extends Module {
    private boolean respawn; // Try to prevent spamming respawn packets

    public AutoRespawn() {
        super("AutoRespawn", new String[]{"respawn"}, Type.PLAYER);
    }

    @Subscribe
    private void onPreUpdate(EventUpdate.Pre event) {
        if (EntityHelper.getUser().getHealth() <= 0) {
            if (!respawn) return;

            EntityHelper.getUser().respawnPlayer();
            respawn = false;
        } else {
            respawn = true;
        }
    }
}
