package io.github.alerithe.client.features.modules.impl.movement.flights;

import io.github.alerithe.client.events.game.EventUpdate;
import io.github.alerithe.client.features.modules.impl.movement.Flight;
import io.github.alerithe.client.utilities.Wrapper;
import net.minecraft.util.MathHelper;

public class SourceEngine extends FlightMode {
    public SourceEngine(Flight module) {
        super("SourceEngine", new String[]{"valve", "source"}, module);
    }

    @Override
    public void onPreUpdate(EventUpdate.Pre event) {
        Wrapper.getPlayer().setSpeed(Wrapper.getPlayer().isUserMoving() ? module.moveSpeed.getValue() : 0);

        float forward = Math.signum(Wrapper.getPlayer().movementInput.moveForward);
        Wrapper.getPlayer().motionY = -MathHelper.deg2Rad * Wrapper.getPlayer().rotationPitch * forward;

        if (forward == 0F) return;
        if (Wrapper.getPlayer().rotationPitch >= 90.0 || Wrapper.getPlayer().rotationPitch <= -90.0) {
            Wrapper.getPlayer().setSpeed(0);
        }
    }
}
