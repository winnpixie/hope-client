package io.github.alerithe.client.features.modules.impl.movement.flights;

import io.github.alerithe.client.events.game.EventMoveUpdate;
import io.github.alerithe.client.features.modules.impl.movement.Flight;
import io.github.alerithe.client.utilities.EntityHelper;
import io.github.alerithe.client.utilities.RotationHelper;

public class SourceEngine extends FlightMode {
    public SourceEngine(Flight module) {
        super("SourceEngine", new String[]{"valve", "source"}, module);
    }

    @Override
    public void onPreUpdate(EventMoveUpdate.Pre event) {
        double speed = module.moveSpeed.getValue();
        float forward = Math.signum(EntityHelper.getUser().movementInput.moveForward);
        EntityHelper.getUser().motionY = -RotationHelper.DEG_TO_RAD * EntityHelper.getUser().rotationPitch * forward * speed;

        if (!EntityHelper.getUser().isUserMoving()
                || EntityHelper.getUser().rotationPitch >= 90f
                || EntityHelper.getUser().rotationPitch <= -90f) {
            EntityHelper.getUser().setSpeed(0.0);
        } else {
            EntityHelper.getUser().setSpeed(speed);
        }
    }
}
