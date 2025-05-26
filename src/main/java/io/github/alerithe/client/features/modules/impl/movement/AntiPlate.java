package io.github.alerithe.client.features.modules.impl.movement;

import io.github.alerithe.events.Register;
import io.github.alerithe.client.events.game.EventUpdate;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.utilities.Wrapper;

public class AntiPlate extends Module {
    private int airTicks;

    public AntiPlate() {
        super("AntiPlate", new String[]{"hover"}, Type.MOVEMENT);
    }

    @Override
    public void onEnable() {
        airTicks = 0;
    }

    @Register
    private void onPreUpdate(EventUpdate.Pre event) {
        if (!Wrapper.getPlayer().isUserMoving()) return;

        if (Wrapper.getPlayer().onGround) {
            Wrapper.getPlayer().jump();
            airTicks = 0;
        } else {
            if (airTicks < 9) {
                airTicks++;
            } else {
                Wrapper.getPlayer().motionY = 0;
                Wrapper.getPlayer().setPosition(Wrapper.getPlayer().posX, Wrapper.getPlayer().posY - 1E-8,
                        Wrapper.getPlayer().posZ);
            }
        }
    }
}
