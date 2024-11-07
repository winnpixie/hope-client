package io.github.alerithe.client.features.modules.impl.player;

import io.github.alerithe.client.events.EventUpdate;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.features.properties.impl.IntProperty;
import io.github.alerithe.client.utilities.Wrapper;
import io.github.alerithe.events.Register;
import net.minecraft.network.play.client.C03PacketPlayer;

public class Regen extends Module {
    private final IntProperty minHealth = new IntProperty("HP", new String[]{"health"}, 6, 1, 19);

    public Regen() {
        super("Regen", new String[0], Type.PLAYER);

        getPropertyManager().add(minHealth);
    }

    @Register
    private void onPreUpdate(EventUpdate.Pre event) {
        if (Wrapper.getPlayer().getHealth() > minHealth.getValue()) return;
        if (Wrapper.getPlayer().getFoodStats().getFoodLevel() < 17) return;
        if (!Wrapper.getPlayer().onGround) return;

        for (int i = 0; i < 20 - Wrapper.getPlayer().getHealth(); i++) {
            Wrapper.sendPacket(new C03PacketPlayer(true));
        }
    }
}
