package io.github.alerithe.client.features.modules.impl.movement.flights;

import io.github.alerithe.client.events.game.EventUpdate;
import io.github.alerithe.client.features.modules.impl.movement.Flight;
import io.github.alerithe.client.utilities.Wrapper;

public class Creative extends FlightMode {
    public Creative(Flight module) {
        super("Creative", new String[0], module);
    }

    @Override
    public void onPreUpdate(EventUpdate.Pre event) {
        Wrapper.getPlayer().setSpeed(Wrapper.getPlayer().isUserMoving() ? module.moveSpeed.getValue() : 0);

        if (Wrapper.getPlayer().movementInput.jump) {
            Wrapper.getPlayer().motionY = 0.5;
        } else if (Wrapper.getPlayer().movementInput.sneak) {
            Wrapper.getPlayer().motionY = -0.5;
        } else {
            Wrapper.getPlayer().motionY = 0;
        }
    }
}
