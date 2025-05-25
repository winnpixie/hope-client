package io.github.alerithe.client.features.modules.impl.player.antiaim;

import io.github.alerithe.client.events.game.EventUpdate;
import io.github.alerithe.client.features.properties.impl.ObjectProperty;

public class RotationMode extends ObjectProperty.Value {
    public RotationMode(String name, String[] aliases) {
        super(name, aliases);
    }

    public void onPreUpdate(EventUpdate.Pre event) {
    }
}
