package io.github.alerithe.client.features.modules.impl.movement.flights;

import io.github.alerithe.client.events.game.EventUpdate;
import io.github.alerithe.client.features.modules.impl.movement.Flight;
import io.github.alerithe.client.utilities.EntityHelper;

public class AncientNCP extends FlightMode {
    public AncientNCP(Flight module) {
        super("AncientNCP", new String[0], module);
    }

    @Override
    public void onPreUpdate(EventUpdate.Pre event) {
        EntityHelper.getUser().motionY = -0.02D;
    }
}
