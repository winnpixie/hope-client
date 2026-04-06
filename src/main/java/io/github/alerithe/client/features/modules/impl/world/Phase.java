package io.github.alerithe.client.features.modules.impl.world;

import io.github.alerithe.client.events.bus.Subscribe;
import io.github.alerithe.client.events.game.EventBlockCollision;
import io.github.alerithe.client.events.game.EventBlockPush;
import io.github.alerithe.client.events.game.EventOpaqueBlockCheck;
import io.github.alerithe.client.events.game.EventUpdate;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.features.modules.impl.world.phase.Alerithe;
import io.github.alerithe.client.features.modules.impl.world.phase.Lemon;
import io.github.alerithe.client.features.modules.impl.world.phase.PhaseMode;
import io.github.alerithe.client.features.properties.impl.ObjectProperty;

public class Phase extends Module {
    private final ObjectProperty<PhaseMode> mode = new ObjectProperty<>("Mode", new String[0], new Alerithe(), new Lemon());

    public Phase() {
        super("Phase", new String[]{"noclip", "faze"}, Type.WORLD);

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
