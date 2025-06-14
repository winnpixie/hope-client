package io.github.alerithe.client.features.modules.impl.movement.flights;

import io.github.alerithe.client.events.game.EventUpdate;
import io.github.alerithe.client.features.modules.impl.movement.Flight;
import io.github.alerithe.client.utilities.EntityHelper;

public class OldNCP extends FlightMode {
    public OldNCP(Flight module) {
        super("OldNCP", new String[0], module);
    }

    @Override
    public void onPreUpdate(EventUpdate.Pre event) {
        EntityHelper.getUser().motionY = 0.0;
    }
}
