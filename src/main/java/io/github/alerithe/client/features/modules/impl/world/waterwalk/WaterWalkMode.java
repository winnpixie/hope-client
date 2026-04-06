package io.github.alerithe.client.features.modules.impl.world.waterwalk;

import io.github.alerithe.client.events.game.EventBlockCollision;
import io.github.alerithe.client.events.game.EventUpdate;
import io.github.alerithe.client.features.properties.impl.ObjectProperty;

public class WaterWalkMode extends ObjectProperty.Value {
    public WaterWalkMode(String name, String... aliases) {
        super(name, aliases);
    }

    public void onPreUpdate(EventUpdate.Pre event) {
    }

    public void onBlockCollide(EventBlockCollision event) {
    }
}
