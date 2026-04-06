package io.github.alerithe.client.features.modules.impl.player.antiaim.yaw;

import io.github.alerithe.client.events.game.EventUpdate;
import io.github.alerithe.client.features.modules.impl.player.antiaim.RotationMode;

public class Backwards extends RotationMode {
    public Backwards() {
        super("Backwards");
    }

    @Override
    public void onPreUpdate(EventUpdate.Pre event) {
        event.setYaw(event.getYaw() + 180f);
    }
}
