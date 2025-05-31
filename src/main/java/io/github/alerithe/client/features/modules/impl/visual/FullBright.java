package io.github.alerithe.client.features.modules.impl.visual;

import io.github.alerithe.client.events.game.EventTick;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.utilities.EntityHelper;
import io.github.alerithe.events.impl.Subscribe;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class FullBright extends Module {
    public FullBright() {
        super("FullBright", new String[]{"brightness"}, Type.VISUAL);
    }

    @Override
    public void onDisable() {
        EntityHelper.getUser().removePotionEffect(Potion.nightVision.id);
    }

    @Subscribe
    private void onStartTick(EventTick.Start event) {
        if (event.isInGame()) EntityHelper.getUser().addPotionEffect(new PotionEffect(Potion.nightVision.id, 5201, 68));
    }
}
