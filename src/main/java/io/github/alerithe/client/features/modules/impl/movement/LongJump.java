package io.github.alerithe.client.features.modules.impl.movement;

import io.github.alerithe.client.events.game.EventUpdate;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.features.properties.impl.DoubleProperty;
import io.github.alerithe.client.utilities.EntityHelper;
import io.github.alerithe.events.impl.Subscribe;

public class LongJump extends Module {
    private final DoubleProperty strength = new DoubleProperty("Boost", new String[0],
            3.3, 0.1, 10.0);

    private boolean boost;

    public LongJump() {
        super("LongJump", new String[]{"lj", "megajump"}, Type.MOVEMENT);

        getPropertyManager().add(strength);
    }

    @Override
    public void onDisable() {
        boost = false;
        EntityHelper.getUser().setSpeed(0);
    }

    @Subscribe
    private void onPreUpdate(EventUpdate.Pre event) {
        if (!EntityHelper.getUser().isUserMoving()) return;

        if (EntityHelper.getUser().onGround && !EntityHelper.getUser().isInLiquid()) {
            EntityHelper.getUser().jump();
            boost = true;
        } else {
            if (boost) {
                EntityHelper.getUser().setSpeed(0.378 * strength.getValue());
                boost = false;
            } else {
                EntityHelper.getUser().setSpeed(EntityHelper.getUser().getSpeed());
            }
        }
    }
}
