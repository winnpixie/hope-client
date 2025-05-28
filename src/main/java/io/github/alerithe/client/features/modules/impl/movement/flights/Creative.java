package io.github.alerithe.client.features.modules.impl.movement.flights;

import io.github.alerithe.client.events.game.EventUpdate;
import io.github.alerithe.client.features.modules.impl.movement.Flight;
import io.github.alerithe.client.utilities.EntityHelper;

public class Creative extends FlightMode {
    public Creative(Flight module) {
        super("Creative", new String[0], module);
    }

    @Override
    public void onPreUpdate(EventUpdate.Pre event) {
        EntityHelper.getUser().setSpeed(EntityHelper.getUser().isUserMoving() ? module.moveSpeed.getValue() : 0);

        if (EntityHelper.getUser().movementInput.jump) {
            EntityHelper.getUser().motionY = 0.5;
        } else if (EntityHelper.getUser().movementInput.sneak) {
            EntityHelper.getUser().motionY = -0.5;
        } else {
            EntityHelper.getUser().motionY = 0;
        }
    }
}
