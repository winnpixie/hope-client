package io.github.alerithe.client.features.modules.impl.world.phase;

import io.github.alerithe.client.events.game.EventBlockCollision;
import io.github.alerithe.client.events.game.EventMoveUpdate;
import io.github.alerithe.client.features.properties.impl.ObjectProperty;

public class PhaseMode extends ObjectProperty.Value {
    public PhaseMode(String name, String... aliases) {
        super(name, aliases);
    }

    public void onPreUpdate(EventMoveUpdate.Pre event) {
    }

    public void onCollision(EventBlockCollision event) {
    }
}
