package io.github.alerithe.client.features.modules.impl.movement;

import io.github.alerithe.client.events.EventUpdate;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.features.modules.impl.movement.flights.*;
import io.github.alerithe.client.features.properties.impl.DoubleProperty;
import io.github.alerithe.client.features.properties.impl.ObjectProperty;
import io.github.alerithe.client.utilities.Wrapper;
import io.github.alerithe.events.Register;

public class Flight extends Module {
    private final ObjectProperty<FlightMode> mode = new ObjectProperty<>("Mode", new String[0], new Creative(this),
            new SkyHigh(this), new OldNCP(this), new AncientNCP(this), new SourceEngine(this));
    public final DoubleProperty moveSpeed = new DoubleProperty("MoveSpeed", new String[]{"speed"},
            1, 0.1, Double.MAX_VALUE);

    public Flight() {
        super("Flight", new String[]{"fly"}, Type.MOVEMENT);

        getPropertyManager().add(mode);
        getPropertyManager().add(moveSpeed);
    }


    @Override
    public void disable() {
        super.disable();

        Wrapper.getPlayer().setSpeed(0);
    }

    @Register
    private void onPreUpdate(EventUpdate.Pre event) {
        mode.getValue().onPreUpdate(event);
    }
}
