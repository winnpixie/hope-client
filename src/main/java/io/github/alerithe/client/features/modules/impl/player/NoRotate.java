package io.github.alerithe.client.features.modules.impl.player;

import io.github.alerithe.events.Register;
import io.github.alerithe.client.events.EventPacket;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.utilities.Wrapper;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

public class NoRotate extends Module {
    public NoRotate() {
        super("NoRotate", new String[0], Type.PLAYER);
    }

    @Register
    private void onPacketRead(EventPacket.Read event) {
        if (!(event.getPacket() instanceof S08PacketPlayerPosLook)) return;
        if (Wrapper.getPlayer() == null) return;

        S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook) event.getPacket();
        event.setPacket(new S08PacketPlayerPosLook(packet.getX(), packet.getY(), packet.getZ(),
                Wrapper.getPlayer().rotationYaw, Wrapper.getPlayer().rotationPitch, packet.func_179834_f()));
    }
}
