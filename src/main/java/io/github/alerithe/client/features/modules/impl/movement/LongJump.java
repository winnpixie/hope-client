package io.github.alerithe.client.features.modules.impl.movement;

import io.github.alerithe.client.events.game.EventUpdate;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.features.properties.impl.DoubleProperty;
import io.github.alerithe.client.utilities.Wrapper;
import io.github.alerithe.events.Register;

public class LongJump extends Module {
    private final DoubleProperty strength = new DoubleProperty("Boost", new String[0], 3.3, 0.1, Double.MAX_VALUE);

    private boolean boost;

    public LongJump() {
        super("LongJump", new String[]{"lj", "megajump"}, Type.MOVEMENT);

        getPropertyManager().add(strength);
    }

    @Override
    public void disable() {
        super.disable();

        boost = false;
        Wrapper.getPlayer().setSpeed(0);
    }

    @Register
    private void onPreUpdate(EventUpdate.Pre event) {
        if (!Wrapper.getPlayer().isUserMoving()) return;

        if (Wrapper.getPlayer().onGround && !Wrapper.getPlayer().isInLiquid()) {
            Wrapper.getPlayer().jump();
            boost = true;
        } else {
            if (boost) {
                Wrapper.getPlayer().setSpeed(0.378 * strength.getValue());
                boost = false;
            } else {
                Wrapper.getPlayer().setSpeed(Wrapper.getPlayer().getSpeed());
            }
        }
    }
}
