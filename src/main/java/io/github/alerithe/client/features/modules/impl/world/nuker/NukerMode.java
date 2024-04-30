package io.github.alerithe.client.features.modules.impl.world.nuker;

import io.github.alerithe.client.events.EventUpdate;
import io.github.alerithe.client.features.properties.impl.ObjectProperty;

public class NukerMode extends ObjectProperty.Value {
    public NukerMode(String name, String[] aliases) {
        super(name, aliases);
    }

    public void onPreUpdate(EventUpdate.Pre event) {
    }

    public void onPostUpdate(EventUpdate.Post event) {
    }
}
