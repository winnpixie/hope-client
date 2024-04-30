package io.github.alerithe.client.features.modules.impl.player.antiaim.yaw;

import io.github.alerithe.client.events.EventUpdate;
import io.github.alerithe.client.features.modules.impl.player.antiaim.RotationMode;

public class Backwards extends RotationMode {
    public Backwards() {
        super("Backwards", new String[0]);
    }

    @Override
    public void onPreUpdate(EventUpdate.Pre event) {
        event.setYaw(event.getYaw() + 180);
    }
}
