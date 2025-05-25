package io.github.alerithe.client.features.modules.impl.miscellaneous;

import io.github.alerithe.client.events.game.EventTick;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.features.properties.impl.DoubleProperty;
import io.github.alerithe.client.utilities.Wrapper;
import io.github.alerithe.events.Register;

public class GameSpeed extends Module {
    private final DoubleProperty speed = new DoubleProperty("Speed", new String[0], 2, 0.1, Double.MAX_VALUE);

    public GameSpeed() {
        super("GameSpeed", new String[]{"timer"}, Type.MISCELLANEOUS);

        getPropertyManager().add(speed);
    }

    @Override
    public void disable() {
        super.disable();

        Wrapper.getGame().timer.timerSpeed = 1f;
    }

    @Register
    private void onTick(EventTick event) {
        Wrapper.getGame().timer.timerSpeed = speed.getValue().floatValue();
    }
}
