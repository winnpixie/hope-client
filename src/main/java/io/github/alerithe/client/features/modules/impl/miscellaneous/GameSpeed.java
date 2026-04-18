package io.github.alerithe.client.features.modules.impl.miscellaneous;

import io.github.alerithe.client.events.bus.Subscribe;
import io.github.alerithe.client.events.game.EventTick;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.features.properties.impl.DoubleProperty;
import io.github.alerithe.client.utilities.GameHelper;

public class GameSpeed extends Module {
    private final DoubleProperty speed = new DoubleProperty("Speed", new String[0],
            2.0, 0.1, 10.0);

    public GameSpeed() {
        super("GameSpeed", new String[]{"timer"}, Type.MISCELLANEOUS);

        getPropertyManager().add(speed);
    }

    @Override
    public void onDisable() {
        GameHelper.getGame().timer.timerSpeed = 1f;
    }

    @Subscribe
    public void onStartTick(EventTick.Start event) {
        GameHelper.getGame().timer.timerSpeed = speed.getValue().floatValue();
    }
}
