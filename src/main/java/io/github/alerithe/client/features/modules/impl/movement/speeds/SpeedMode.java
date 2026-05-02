package io.github.alerithe.client.features.modules.impl.movement.speeds;

import io.github.alerithe.client.events.game.EventMoveUpdate;
import io.github.alerithe.client.features.properties.impl.ObjectProperty;

public class SpeedMode extends ObjectProperty.Value {
    public SpeedMode(String name, String... aliases) {
        super(name, aliases);
    }

    public void onPreUpdate(EventMoveUpdate.Pre event) {
    }
}
