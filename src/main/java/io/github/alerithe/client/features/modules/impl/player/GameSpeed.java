package io.github.alerithe.client.features.modules.impl.player;

import io.github.alerithe.client.events.EventTick;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.features.properties.impl.NumberProperty;
import io.github.alerithe.client.utilities.Wrapper;
import io.github.alerithe.events.Register;

public class GameSpeed extends Module {
    private final NumberProperty<Double> speed = new NumberProperty<>("Speed", new String[0], 2d, 0.1d, Double.MAX_VALUE);

    public GameSpeed() {
        super("GameSpeed", new String[]{"timer"}, Type.PLAYER);

        getPropertyManager().add(speed);
    }

    @Override
    public void disable() {
        super.disable();

        Wrapper.getMC().timer.timerSpeed = 1f;
    }

    @Register
    private void onTick(EventTick event) {
        Wrapper.getMC().timer.timerSpeed = speed.getValue().floatValue();
    }
}
