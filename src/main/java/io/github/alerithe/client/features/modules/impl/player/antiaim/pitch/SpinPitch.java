package io.github.alerithe.client.features.modules.impl.player.antiaim.pitch;

import io.github.alerithe.client.events.game.EventMoveUpdate;
import io.github.alerithe.client.features.modules.impl.player.antiaim.RotationMode;

public class SpinPitch extends RotationMode {
    public SpinPitch() {
        super("Spin");
    }

    @Override
    public void onPreUpdate(EventMoveUpdate.Pre event) {
        event.setPitch(((float) (System.currentTimeMillis() / 5.0 % 360)));
    }
}
