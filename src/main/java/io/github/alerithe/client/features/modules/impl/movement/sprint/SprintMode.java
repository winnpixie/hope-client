package io.github.alerithe.client.features.modules.impl.movement.sprint;

import io.github.alerithe.client.events.game.EventMoveUpdate;
import io.github.alerithe.client.features.modules.impl.movement.AutoSprint;
import io.github.alerithe.client.features.properties.impl.ObjectProperty;

public class SprintMode extends ObjectProperty.Value {
    protected final AutoSprint module;

    public SprintMode(String name, String[] aliases, AutoSprint module) {
        super(name, aliases);

        this.module = module;
    }

    public void onPreUpdate(EventMoveUpdate.Pre event) {
    }
}
