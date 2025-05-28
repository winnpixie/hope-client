package io.github.alerithe.client.features.modules.impl.movement.speeds;

import io.github.alerithe.client.events.game.EventUpdate;
import io.github.alerithe.client.utilities.EntityHelper;
import io.github.alerithe.client.utilities.WorldHelper;

public class Offset extends SpeedMode {
    public Offset() {
        super("Offset", new String[]{"latest"});
    }

    @Override
    public void onPreUpdate(EventUpdate.Pre event) {
        if (!EntityHelper.getUser().isUserMoving()) return;
        if (!EntityHelper.getUser().onGround) return;
        if (EntityHelper.getUser().movementInput.jump) return;
        if (EntityHelper.getUser().isInLiquid()) return;

        boolean boost = EntityHelper.getUser().ticksExisted % 2 == 0;
        boolean clearerSkies = WorldHelper.getWorld().getCollidingBoundingBoxes(EntityHelper.getUser(),
                EntityHelper.getUser().getEntityBoundingBox().offset(0, 0.3, 0)).isEmpty();
        boolean clearSkies = WorldHelper.getWorld().getCollidingBoundingBoxes(EntityHelper.getUser(),
                EntityHelper.getUser().getEntityBoundingBox().offset(0, 0.02, 0)).isEmpty();

        if (!boost) {
            event.setY(event.getY() + (clearerSkies ? 0.4 : clearSkies ? 0.2 : 0.0125));
            event.setOnGround(false);
        }

        EntityHelper.getUser().setSpeed(EntityHelper.getUser().getSpeed() * (boost ? 2 : 0.705));
    }
}
