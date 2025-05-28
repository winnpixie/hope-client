package io.github.alerithe.client.features.modules.impl.miscellaneous;

import io.github.alerithe.client.events.game.EventTick;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.features.properties.impl.DoubleProperty;
import io.github.alerithe.client.utilities.GameHelper;
import io.github.alerithe.events.impl.Subscribe;

public class GameSpeed extends Module {
    private final DoubleProperty speed = new DoubleProperty("Speed", new String[0],
            2.0, 0.1, 100.0);

    public GameSpeed() {
        super("GameSpeed", new String[]{"timer"}, Type.MISCELLANEOUS);

        getPropertyManager().add(speed);
    }

    @Override
    public void onDisable() {
        GameHelper.getGame().timer.timerSpeed = 1f;
    }

    @Subscribe
    private void onTick(EventTick event) {
        GameHelper.getGame().timer.timerSpeed = speed.getValue().floatValue();
    }
}
