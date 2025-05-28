package io.github.alerithe.client.features.modules.impl.movement.flights;

import io.github.alerithe.client.events.game.EventUpdate;
import io.github.alerithe.client.features.modules.impl.movement.Flight;
import io.github.alerithe.client.utilities.EntityHelper;
import net.minecraft.util.MathHelper;

public class SourceEngine extends FlightMode {
    public SourceEngine(Flight module) {
        super("SourceEngine", new String[]{"valve", "source"}, module);
    }

    @Override
    public void onPreUpdate(EventUpdate.Pre event) {
        EntityHelper.getUser().setSpeed(EntityHelper.getUser().isUserMoving() ? module.moveSpeed.getValue() : 0);

        float forward = Math.signum(EntityHelper.getUser().movementInput.moveForward);
        EntityHelper.getUser().motionY = -MathHelper.deg2Rad * EntityHelper.getUser().rotationPitch * forward;

        if (forward == 0F) return;
        if (EntityHelper.getUser().rotationPitch >= 90.0 || EntityHelper.getUser().rotationPitch <= -90.0) {
            EntityHelper.getUser().setSpeed(0);
        }
    }
}
