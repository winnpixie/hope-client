package io.github.alerithe.client.features.modules.impl.movement.speeds;

import io.github.alerithe.client.events.game.EventUpdate;
import io.github.alerithe.client.utilities.Wrapper;

public class OldOffset extends SpeedMode {
    public OldOffset() {
        super("OldOffset", new String[]{"old"});
    }

    @Override
    public void onPreUpdate(EventUpdate.Pre event) {
        if (!Wrapper.getPlayer().isUserMoving()) return;
        if (!Wrapper.getPlayer().onGround) return;
        if (Wrapper.getPlayer().movementInput.jump) return;
        if (Wrapper.getPlayer().isInLiquid()) return;

        boolean boost = Wrapper.getPlayer().ticksExisted % 2 == 0;
        boolean clearerSkies = Wrapper.getWorld().getCollidingBoundingBoxes(Wrapper.getPlayer(),
                Wrapper.getPlayer().getEntityBoundingBox().offset(0, 0.3, 0)).isEmpty();
        boolean clearSkies = Wrapper.getWorld().getCollidingBoundingBoxes(Wrapper.getPlayer(),
                Wrapper.getPlayer().getEntityBoundingBox().offset(0, 0.02, 0)).isEmpty();

        if (!boost) {
            event.setY(event.getY() + (clearerSkies ? 0.4 : clearSkies ? 0.2 : 0.0125));
            event.setOnGround(false);
        }

        Wrapper.getPlayer().setSpeed(Wrapper.getPlayer().getSpeed() * (boost ? 3.3 : 0.705));
    }
}
