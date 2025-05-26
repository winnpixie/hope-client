package io.github.alerithe.client.features.modules.impl.visual;

import io.github.alerithe.client.events.game.EventTick;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.utilities.Wrapper;
import io.github.alerithe.events.Register;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class FullBright extends Module {
    public FullBright() {
        super("FullBright", new String[]{"brightness"}, Type.VISUAL);
    }

    @Override
    public void onDisable() {
        Wrapper.getPlayer().removePotionEffect(Potion.nightVision.id);
    }

    @Register
    private void onTick(EventTick event) {
        if (event.isInGame()) Wrapper.getPlayer().addPotionEffect(new PotionEffect(Potion.nightVision.id, 5201, 68));
    }
}
