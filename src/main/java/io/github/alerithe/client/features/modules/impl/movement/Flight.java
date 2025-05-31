package io.github.alerithe.client.features.modules.impl.movement;

import io.github.alerithe.client.events.game.EventUpdate;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.features.modules.impl.movement.flights.*;
import io.github.alerithe.client.features.properties.impl.DoubleProperty;
import io.github.alerithe.client.features.properties.impl.ObjectProperty;
import io.github.alerithe.client.utilities.EntityHelper;
import io.github.alerithe.events.impl.Subscribe;

public class Flight extends Module {
    private final ObjectProperty<FlightMode> mode = new ObjectProperty<>("Mode", new String[0], new Creative(this),
            new SkyHigh(this), new OldNCP(this), new Vanilla(this), new SourceEngine(this));
    public final DoubleProperty moveSpeed = new DoubleProperty("MoveSpeed", new String[]{"speed"},
            1.0, 0.1, 10.0);

    public Flight() {
        super("Flight", new String[]{"fly"}, Type.MOVEMENT);

        getPropertyManager().add(mode);
        getPropertyManager().add(moveSpeed);
    }


    @Override
    public void onDisable() {
        EntityHelper.getUser().setSpeed(0);
    }

    @Subscribe
    private void onPreUpdate(EventUpdate.Pre event) {
        mode.getValue().onPreUpdate(event);
    }
}
