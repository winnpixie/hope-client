package io.github.alerithe.client.features.modules.impl.player;

import io.github.alerithe.client.events.game.EventGameScreen;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.utilities.EntityHelper;
import io.github.alerithe.events.impl.Subscribe;
import net.minecraft.client.gui.GuiGameOver;

public class AutoRespawn extends Module {
    public AutoRespawn() {
        super("AutoRespawn", new String[]{"respawn"}, Type.PLAYER);
    }

    @Subscribe
    private void onScreenOpen(EventGameScreen.Open event) {
        if (!(event.getScreen() instanceof GuiGameOver)) return;

        // TODO: Add configurable delay?
        EntityHelper.getUser().respawnPlayer();
    }
}
