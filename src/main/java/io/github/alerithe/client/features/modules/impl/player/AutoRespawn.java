package io.github.alerithe.client.features.modules.impl.player;

import io.github.alerithe.client.events.game.EventUpdate;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.utilities.Wrapper;
import io.github.alerithe.events.Register;

public class AutoRespawn extends Module {
    private boolean respawn; // Try to prevent spamming respawn packets

    public AutoRespawn() {
        super("AutoRespawn", new String[]{"respawn"}, Type.PLAYER);
    }

    @Register
    private void onPreUpdate(EventUpdate.Pre event) {
        if (Wrapper.getPlayer().getHealth() <= 0) {
            if (!respawn) return;

            Wrapper.getPlayer().respawnPlayer();
            respawn = false;
        } else {
            respawn = true;
        }
    }
}
