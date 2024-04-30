package io.github.alerithe.client.features.modules.impl.movement.flights;

import io.github.alerithe.client.events.EventUpdate;
import io.github.alerithe.client.features.modules.impl.movement.Flight;
import io.github.alerithe.client.utilities.Wrapper;
import net.minecraft.util.MathHelper;

public class SourceEngine extends FlightMode {
    public SourceEngine(Flight module) {
        super("SourceEngine", new String[]{"valve", "source"}, module);
    }

    @Override
    public void onPreUpdate(EventUpdate.Pre event) {

        if (!Wrapper.getPlayer().isUserMoving()) {
            Wrapper.getPlayer().motionY = 0;
            Wrapper.getPlayer().setSpeed(0);
            return;
        }

        Wrapper.getPlayer().setSpeed(module.moveSpeed.getValue());
        Wrapper.getPlayer().motionY = -MathHelper.deg2Rad * Wrapper.getPlayer().rotationPitch;

        if (Wrapper.getPlayer().rotationPitch >= 90.0 || Wrapper.getPlayer().rotationPitch <= -90.0) {
            Wrapper.getPlayer().setSpeed(0);
        }
    }
}
