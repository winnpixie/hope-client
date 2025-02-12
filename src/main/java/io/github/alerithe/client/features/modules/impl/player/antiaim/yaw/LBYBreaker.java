package io.github.alerithe.client.features.modules.impl.player.antiaim.yaw;

import io.github.alerithe.client.events.EventUpdate;
import io.github.alerithe.client.features.modules.impl.player.antiaim.RotationMode;
import io.github.alerithe.client.utilities.MathHelper;
import io.github.alerithe.client.utilities.Wrapper;

public class LBYBreaker extends RotationMode {
    public LBYBreaker() {
        super("LBY", new String[0]);
    }

    @Override
    public void onPreUpdate(EventUpdate.Pre event) {
        if (Wrapper.getPlayer().ticksExisted % 10 == 0) {
            event.setYaw(event.getYaw() + 90);
            return;
        }

        event.setYaw(MathHelper.clamp(event.getYaw() + MathHelper.getRandomFloat(-15f, 15f), -180f, 180f));
    }
}
