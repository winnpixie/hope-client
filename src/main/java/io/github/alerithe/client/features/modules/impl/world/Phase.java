package io.github.alerithe.client.features.modules.impl.world;

import io.github.alerithe.client.events.game.EventBlockCollision;
import io.github.alerithe.client.events.game.EventBlockPush;
import io.github.alerithe.client.events.game.EventOpaqueBlockCheck;
import io.github.alerithe.client.events.game.EventUpdate;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.features.modules.impl.world.phase.Latest;
import io.github.alerithe.client.features.modules.impl.world.phase.NullAABB;
import io.github.alerithe.client.features.modules.impl.world.phase.PhaseMode;
import io.github.alerithe.client.features.properties.impl.ObjectProperty;
import io.github.alerithe.events.impl.Subscribe;

public class Phase extends Module {
    private final ObjectProperty<PhaseMode> mode = new ObjectProperty<>("Mode", new String[0], new Latest(), new NullAABB());

    public Phase() {
        super("Phase", new String[]{"faze"}, Type.WORLD);

        getPropertyManager().add(mode);
    }

    @Subscribe
    private void onPreUpdate(EventUpdate.Pre event) {
        mode.getValue().onPreUpdate(event);
    }

    @Subscribe
    private void onOpaqueCheck(EventOpaqueBlockCheck event) {
        event.cancel();
    }

    @Subscribe
    private void onBlockPush(EventBlockPush event) {
        event.cancel();
    }

    @Subscribe
    private void onCollision(EventBlockCollision event) {
        mode.getValue().onCollision(event);
    }
}
