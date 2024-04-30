package io.github.alerithe.client.features.modules.impl.player.antiaim.yaw;

import io.github.alerithe.client.events.EventUpdate;
import io.github.alerithe.client.features.modules.impl.player.antiaim.RotationMode;

public class SpinYaw extends RotationMode {
    public SpinYaw() {
        super("Spin", new String[0]);
    }

    @Override
    public void onPreUpdate(EventUpdate.Pre event) {
        event.setYaw(System.currentTimeMillis() / 10 % 360);
    }
}
