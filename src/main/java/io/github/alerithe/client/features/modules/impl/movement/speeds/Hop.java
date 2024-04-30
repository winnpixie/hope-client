package io.github.alerithe.client.features.modules.impl.movement.speeds;

import io.github.alerithe.client.events.EventUpdate;
import io.github.alerithe.client.utilities.Wrapper;

public class Hop extends SpeedMode {
    private boolean boost;

    public Hop() {
        super("Hop", new String[]{"bhop"});
    }

    @Override
    public void onPreUpdate(EventUpdate.Pre event) {
        if (!Wrapper.getPlayer().isUserMoving()) return;
        if (Wrapper.getPlayer().movementInput.jump) return;
        if (Wrapper.getPlayer().isInLiquid()) return;

        if (Wrapper.getPlayer().onGround) {
            Wrapper.getPlayer().setPosition(Wrapper.getPlayer().posX, Wrapper.getPlayer().posY + 0.5,
                    Wrapper.getPlayer().posZ);
            Wrapper.getPlayer().setSpeed(0.34999);
            boost = true;
        } else {
            if (boost) {
                boost = false;
                Wrapper.getPlayer().motionY = -0.5;
            }

            Wrapper.getPlayer().setSpeed(Wrapper.getPlayer().getSpeed());
        }
    }
}
