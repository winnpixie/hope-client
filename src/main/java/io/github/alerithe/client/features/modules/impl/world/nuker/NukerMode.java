package io.github.alerithe.client.features.modules.impl.world.nuker;

import io.github.alerithe.client.events.game.EventMoveUpdate;
import io.github.alerithe.client.features.modules.impl.world.Nuker;
import io.github.alerithe.client.features.properties.impl.ObjectProperty;

public class NukerMode extends ObjectProperty.Value {
    protected final Nuker module;

    public NukerMode(String name, String[] aliases, Nuker module) {
        super(name, aliases);

        this.module = module;
    }

    public void onPreUpdate(EventMoveUpdate.Pre event) {
    }

    public void onPostUpdate(EventMoveUpdate.Post event) {
    }
}
