package io.github.alerithe.client.features.modules.impl.world;

import io.github.alerithe.client.events.bus.Subscribe;
import io.github.alerithe.client.events.game.EventBlockCollision;
import io.github.alerithe.client.events.game.EventUpdate;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.features.modules.impl.world.waterwalk.Bounce;
import io.github.alerithe.client.features.modules.impl.world.waterwalk.Solid;
import io.github.alerithe.client.features.modules.impl.world.waterwalk.WaterWalkMode;
import io.github.alerithe.client.features.properties.impl.ObjectProperty;

public class WaterWalk extends Module {
    private final ObjectProperty<WaterWalkMode> mode = new ObjectProperty<>("Mode", new String[0], new Solid(), new Bounce());

    public WaterWalk() {
        super("WaterWalk", new String[]{"jesus"}, Type.WORLD);

        getPropertyManager().add(mode);
    }

    @Subscribe
    public void onPreUpdate(EventUpdate.Pre event) {
        mode.getValue().onPreUpdate(event);
    }

    @Subscribe
    public void onBlockCollide(EventBlockCollision event) {
        mode.getValue().onBlockCollide(event);
    }
}
