package io.github.alerithe.client.features.modules.impl.player.antiaim.pitch;

import io.github.alerithe.client.events.game.EventUpdate;
import io.github.alerithe.client.features.modules.impl.player.antiaim.RotationMode;

public class SpinPitch extends RotationMode {
    public SpinPitch() {
        super("Spin", new String[0]);
    }

    @Override
    public void onPreUpdate(EventUpdate.Pre event) {
        event.setPitch(((float) (System.currentTimeMillis() / 5.0 % 360)));
    }
}
