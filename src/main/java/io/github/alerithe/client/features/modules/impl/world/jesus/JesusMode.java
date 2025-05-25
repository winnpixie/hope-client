package io.github.alerithe.client.features.modules.impl.world.jesus;

import io.github.alerithe.client.events.game.EventBlockCollision;
import io.github.alerithe.client.events.game.EventUpdate;
import io.github.alerithe.client.features.properties.impl.ObjectProperty;

public class JesusMode extends ObjectProperty.Value {
    public JesusMode(String name, String[] aliases) {
        super(name, aliases);
    }

    public void onPreUpdate(EventUpdate.Pre event) {
    }

    public void onBlockCollide(EventBlockCollision event) {
    }
}
