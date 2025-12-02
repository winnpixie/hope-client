package io.github.alerithe.client.features.modules.impl.movement.step;

import io.github.alerithe.client.events.game.EventUpdate;
import io.github.alerithe.client.features.modules.impl.movement.Step;
import io.github.alerithe.client.utilities.EntityHelper;

public class Motion extends StepMode {
    public Motion(Step module) {
        super("Motion", new String[0], module);
    }

    @Override
    public void onPreUpdate(EventUpdate.Pre event) {
        if (!EntityHelper.getUser().isCollidedHorizontally) return;
        if (EntityHelper.getUser().movementInput.jump) return;
        if (EntityHelper.getUser().isInLiquid()) return;

        if (EntityHelper.getUser().onGround) {
            EntityHelper.getUser().motionY = 0.369;
        } else if (module.oneAndAHalf.getValue() && EntityHelper.getUser().fallDistance > 0) {
            EntityHelper.getUser().motionY = 0.26;
            EntityHelper.getUser().fallDistance = 0;
        }
    }
}
