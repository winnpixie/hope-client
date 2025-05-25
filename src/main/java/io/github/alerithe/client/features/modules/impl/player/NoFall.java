package io.github.alerithe.client.features.modules.impl.player;

import io.github.alerithe.client.events.game.EventUpdate;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.utilities.Wrapper;
import io.github.alerithe.events.Register;

public class NoFall extends Module {
    public NoFall() {
        super("NoFall", new String[0], Type.PLAYER);
    }

    @Register
    private void onPreUpdate(EventUpdate.Pre event) {
        if (Wrapper.getPlayer().fallDistance > 3) event.setOnGround(true);
    }
}
