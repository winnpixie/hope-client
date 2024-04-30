package io.github.alerithe.client.features.modules.impl.movement.flights;

import io.github.alerithe.client.events.EventUpdate;
import io.github.alerithe.client.features.modules.impl.movement.Flight;
import io.github.alerithe.client.utilities.Wrapper;

public class OldNCP extends FlightMode {
    public OldNCP(Flight module) {
        super("OldNCP", new String[0], module);
    }

    @Override
    public void onPreUpdate(EventUpdate.Pre event) {
        Wrapper.getPlayer().motionY = 0.0;
    }
}
