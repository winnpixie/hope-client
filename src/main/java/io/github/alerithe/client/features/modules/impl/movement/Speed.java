package io.github.alerithe.client.features.modules.impl.movement;

import io.github.alerithe.client.events.bus.Subscribe;
import io.github.alerithe.client.events.game.EventUpdate;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.features.modules.impl.movement.speeds.Alerithe;
import io.github.alerithe.client.features.modules.impl.movement.speeds.Atom;
import io.github.alerithe.client.features.modules.impl.movement.speeds.Hop;
import io.github.alerithe.client.features.modules.impl.movement.speeds.SpeedMode;
import io.github.alerithe.client.features.properties.impl.ObjectProperty;

public class Speed extends Module {
    private final ObjectProperty<SpeedMode> mode = new ObjectProperty<>("Mode", new String[0], new Alerithe(), new Hop(),
            new Atom());

    public Speed() {
        super("Speed", new String[0], Type.MOVEMENT);

        getPropertyManager().add(mode);
    }

    @Subscribe
    public void onPreUpdate(EventUpdate.Pre event) {
        mode.getValue().onPreUpdate(event);
    }
}
