package io.github.alerithe.client.features.modules.impl.player;

import io.github.alerithe.client.events.game.EventUpdate;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.features.properties.impl.IntProperty;
import io.github.alerithe.client.utilities.EntityHelper;
import io.github.alerithe.client.utilities.NetworkHelper;
import io.github.alerithe.events.impl.Subscribe;
import net.minecraft.network.play.client.C03PacketPlayer;

public class Regen extends Module {
    private final IntProperty minHealth = new IntProperty("HP", new String[]{"health"}, 6, 1, 19);

    public Regen() {
        super("Regen", new String[0], Type.PLAYER);

        getPropertyManager().add(minHealth);
    }

    @Subscribe
    private void onPreUpdate(EventUpdate.Pre event) {
        if (EntityHelper.getUser().getHealth() > minHealth.getValue()) return;
        if (EntityHelper.getUser().getFoodStats().getFoodLevel() < 17) return;
        if (!EntityHelper.getUser().onGround) return;

        for (int i = 0; i < 20 - EntityHelper.getUser().getHealth(); i++) {
            NetworkHelper.sendPacket(new C03PacketPlayer(true));
        }
    }
}
