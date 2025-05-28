package io.github.alerithe.client.features.modules.impl.movement.speeds;

import io.github.alerithe.client.events.game.EventUpdate;
import io.github.alerithe.client.utilities.EntityHelper;

public class Hop extends SpeedMode {
    private boolean boost;

    public Hop() {
        super("Hop", new String[]{"bhop"});
    }

    @Override
    public void onPreUpdate(EventUpdate.Pre event) {
        if (!EntityHelper.getUser().isUserMoving()) return;
        if (EntityHelper.getUser().movementInput.jump) return;
        if (EntityHelper.getUser().isInLiquid()) return;

        if (EntityHelper.getUser().onGround) {
            EntityHelper.getUser().setPosition(EntityHelper.getUser().posX, EntityHelper.getUser().posY + 0.5,
                    EntityHelper.getUser().posZ);
            EntityHelper.getUser().setSpeed(0.34999);
            boost = true;
        } else {
            if (boost) {
                boost = false;
                EntityHelper.getUser().motionY = -0.5;
            }

            EntityHelper.getUser().setSpeed(EntityHelper.getUser().getSpeed());
        }
    }
}
