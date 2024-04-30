package io.github.alerithe.client.features.modules.impl.world;

import io.github.alerithe.client.events.EventUpdate;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.features.modules.impl.world.nuker.Creative;
import io.github.alerithe.client.features.modules.impl.world.nuker.NukerMode;
import io.github.alerithe.client.features.modules.impl.world.nuker.Survival;
import io.github.alerithe.client.features.properties.impl.ObjectProperty;
import io.github.alerithe.events.Register;

public class Nuker extends Module {
    private final ObjectProperty<NukerMode> mode = new ObjectProperty<>("Mode", new String[0], new Creative(), new Survival());

    public Nuker() {
        super("Nuker", new String[0], Type.WORLD);

        getPropertyManager().add(mode);
    }

    @Register
    private void onPreUpdate(EventUpdate.Pre event) {
        mode.getValue().onPreUpdate(event);
    }

    @Register
    private void onPostUpdate(EventUpdate.Post event) {
        mode.getValue().onPostUpdate(event);
    }
}
