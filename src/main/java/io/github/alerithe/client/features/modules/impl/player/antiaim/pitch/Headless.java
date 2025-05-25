package io.github.alerithe.client.features.modules.impl.player.antiaim.pitch;

import io.github.alerithe.client.events.game.EventUpdate;
import io.github.alerithe.client.features.modules.impl.player.antiaim.RotationMode;

public class Headless extends RotationMode {
    public Headless() {
        super("Headless", new String[0]);
    }

    @Override
    public void onPreUpdate(EventUpdate.Pre event) {
        event.setPitch(180);
    }
}
