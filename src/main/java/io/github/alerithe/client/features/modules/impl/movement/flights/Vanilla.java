package io.github.alerithe.client.features.modules.impl.movement.flights;

import io.github.alerithe.client.events.game.EventUpdate;
import io.github.alerithe.client.features.modules.impl.movement.Flight;
import io.github.alerithe.client.utilities.EntityHelper;

public class Vanilla extends FlightMode {
    public Vanilla(Flight module) {
        super("Vanilla", new String[0], module);
    }

    @Override
    public void onPreUpdate(EventUpdate.Pre event) {
        // You must fall at LEAST 1/32 a block to prevent "Flying is not allowed on this server" kick
        double minFallDelta = 1.0 / 31.9;
        EntityHelper.getUser().motionY = -minFallDelta;
    }
}
