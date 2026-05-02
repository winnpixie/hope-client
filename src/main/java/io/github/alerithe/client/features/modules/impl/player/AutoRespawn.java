package io.github.alerithe.client.features.modules.impl.player;

import io.github.alerithe.client.events.bus.Subscribe;
import io.github.alerithe.client.events.game.EventGameScreen;
import io.github.alerithe.client.events.game.EventTick;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.utilities.EntityHelper;
import net.minecraft.client.gui.GuiGameOver;

public class AutoRespawn extends Module {
    private boolean dead;

    public AutoRespawn() {
        super("AutoRespawn", new String[]{"respawn"}, Type.PLAYER);
    }

    @Subscribe
    public void onScreenOpen(EventGameScreen.Open event) {
        if (event.getScreen() instanceof GuiGameOver) {
            this.dead = true;
        }
    }

    @Subscribe
    public void onTickStart(EventTick.Start event) {
        if (dead) {
            EntityHelper.getUser().respawnPlayer();

            this.dead = false;
        }
    }
}
