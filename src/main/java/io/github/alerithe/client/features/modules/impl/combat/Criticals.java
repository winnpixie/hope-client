package io.github.alerithe.client.features.modules.impl.combat;

import io.github.alerithe.client.events.game.EventPacket;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.features.properties.impl.BooleanProperty;
import io.github.alerithe.client.utilities.Wrapper;
import io.github.alerithe.events.Register;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;

public class Criticals extends Module {
    private final BooleanProperty packets = new BooleanProperty("Packets", new String[0], true);

    // FIXME: This module only works on Odie, what did I do SO wrong??
    private final double[] offsets = {0.0625, 0.0, 0.000011, 0.0};

    public Criticals() {
        super("Criticals", new String[]{"crits"}, Type.COMBAT);

        getPropertyManager().add(packets);
    }

    @Register
    private void onPacketWrite(EventPacket.Write event) {
        if (!(event.getPacket() instanceof C02PacketUseEntity)) return;

        C02PacketUseEntity packet = (C02PacketUseEntity) event.getPacket();
        if (packet.getAction() != C02PacketUseEntity.Action.ATTACK) return;
        if (!Wrapper.getPlayer().onGround) return;

        if (!packets.getValue()) {
            Wrapper.getPlayer().motionY = 0.42;
            return;
        }

        for (double offset : offsets) {
            event.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(
                    Wrapper.getPlayer().posX, Wrapper.getPlayer().posY + offset,
                    Wrapper.getPlayer().posZ, offset == 0.0));
        }
    }
}
