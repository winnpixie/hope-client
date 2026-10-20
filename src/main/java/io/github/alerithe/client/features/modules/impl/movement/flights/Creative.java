package io.github.alerithe.client.features.modules.impl.movement.flights;

import io.github.alerithe.client.events.game.EventMoveUpdate;
import io.github.alerithe.client.features.modules.impl.movement.Flight;
import io.github.alerithe.client.utilities.EntityHelper;

public class Creative extends FlightMode {
    public Creative(Flight module) {
        super("Creative", new String[0], module);
    }

    @Override
    public void onPreUpdate(EventMoveUpdate.Pre event) {
        double speed = module.moveSpeed.getValue();
        EntityHelper.getUser().setSpeed(EntityHelper.getUser().isUserMoving() ? speed : 0.0);

        if (EntityHelper.getUser().movementInput.jump) {
            EntityHelper.getUser().motionY = speed / 2.0;
        } else if (EntityHelper.getUser().movementInput.sneak) {
            EntityHelper.getUser().motionY = -(speed / 2.0);
        } else {
            EntityHelper.getUser().motionY = 0;
        }
    }
}
