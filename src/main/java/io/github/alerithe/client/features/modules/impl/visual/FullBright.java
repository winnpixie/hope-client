package io.github.alerithe.client.features.modules.impl.visual;

import io.github.alerithe.client.events.bus.Subscribe;
import io.github.alerithe.client.events.game.EventTick;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.utilities.GameHelper;

public class FullBright extends Module {
    private float userGamma;

    public FullBright() {
        super("FullBright", new String[]{"brightness", "gamma"}, Type.VISUAL);
    }

    @Override
    public void onEnable() {
        this.userGamma = GameHelper.getSettings().gammaSetting;
    }

    @Override
    public void onDisable() {
        GameHelper.getSettings().gammaSetting = userGamma;
    }

    @Subscribe
    public void onStartTick(EventTick.Start event) {
        // attempt to update the restore value if the player changed it while toggled
        float currentGamma = GameHelper.getSettings().gammaSetting;
        if (currentGamma < 10f) {
            this.userGamma = currentGamma;
        }

        GameHelper.getSettings().gammaSetting = 10f;
    }
}
