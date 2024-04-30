package io.github.alerithe.client.features.modules.impl.world;

import io.github.alerithe.client.events.EventBlockCollision;
import io.github.alerithe.client.events.EventBlockPush;
import io.github.alerithe.client.events.EventOpaqueCheck;
import io.github.alerithe.client.events.EventUpdate;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.features.modules.impl.world.phase.Latest;
import io.github.alerithe.client.features.modules.impl.world.phase.NullAABB;
import io.github.alerithe.client.features.modules.impl.world.phase.PhaseMode;
import io.github.alerithe.client.features.properties.impl.ObjectProperty;
import io.github.alerithe.events.Register;

public class Phase extends Module {
    private final ObjectProperty<PhaseMode> mode = new ObjectProperty<>("Mode", new String[0], new Latest(), new NullAABB());

    public Phase() {
        super("Phase", new String[]{"faze"}, Type.WORLD);

        getPropertyManager().add(mode);
    }

    @Register
    private void onPreUpdate(EventUpdate.Pre event) {
        mode.getValue().onPreUpdate(event);
    }

    @Register
    private void onOpaqueCheck(EventOpaqueCheck event) {
        event.setCancelled(true);
    }

    @Register
    private void onBlockPush(EventBlockPush event) {
        event.setCancelled(true);
    }

    @Register
    private void onCollision(EventBlockCollision event) {
        mode.getValue().onCollision(event);
    }
}
