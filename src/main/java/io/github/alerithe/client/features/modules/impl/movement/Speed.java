package io.github.alerithe.client.features.modules.impl.movement;

import io.github.alerithe.client.events.game.EventUpdate;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.features.modules.impl.movement.speeds.Hop;
import io.github.alerithe.client.features.modules.impl.movement.speeds.Offset;
import io.github.alerithe.client.features.modules.impl.movement.speeds.OldOffset;
import io.github.alerithe.client.features.modules.impl.movement.speeds.SpeedMode;
import io.github.alerithe.client.features.properties.impl.ObjectProperty;
import io.github.alerithe.events.Register;

public class Speed extends Module {
    private final ObjectProperty<SpeedMode> mode = new ObjectProperty<>("Mode", new String[0], new Offset(), new Hop(),
            new OldOffset());

    public Speed() {
        super("Speed", new String[0], Type.MOVEMENT);

        getPropertyManager().add(mode);
    }

    @Register
    private void onPreUpdate(EventUpdate.Pre event) {
        mode.getValue().onPreUpdate(event);
    }
}
