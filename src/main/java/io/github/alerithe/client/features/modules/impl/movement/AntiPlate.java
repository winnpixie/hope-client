package io.github.alerithe.client.features.modules.impl.movement;

import io.github.alerithe.client.events.game.EventUpdate;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.utilities.EntityHelper;
import io.github.alerithe.client.utilities.GameHelper;
import io.github.alerithe.events.impl.Subscribe;

public class AntiPlate extends Module {
    private int airTicks;

    public AntiPlate() {
        super("AntiPlate", new String[]{"hover"}, Type.MOVEMENT);
    }

    @Override
    public void onEnable() {
        airTicks = 0;
    }

    @Subscribe
    private void onPreUpdate(EventUpdate.Pre event) {
        if (!EntityHelper.getUser().isUserMoving()) return;

        if (EntityHelper.getUser().onGround) {
            EntityHelper.getUser().motionY = 0.422;
            airTicks = 0;
        } else {
            if (airTicks < 9) {
                airTicks++;
            } else {
                EntityHelper.getUser().motionY = 0;
                EntityHelper.getUser().setPosition(EntityHelper.getUser().posX, EntityHelper.getUser().posY - 1E-8,
                        EntityHelper.getUser().posZ);
            }
        }
    }
}
