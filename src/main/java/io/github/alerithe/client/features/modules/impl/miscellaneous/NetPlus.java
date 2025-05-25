package io.github.alerithe.client.features.modules.impl.miscellaneous;

import io.github.alerithe.client.events.game.EventPacket;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.utilities.Wrapper;
import io.github.alerithe.events.Register;
import net.minecraft.network.play.client.C03PacketPlayer;

public class NetPlus extends Module {
    public NetPlus() {
        super("Net+", new String[]{"netplus"}, Type.MISCELLANEOUS);
    }

    @Register
    private void onPacketWrite(EventPacket.Write event) {
        if (event.getPacket() instanceof C03PacketPlayer.C04PacketPlayerPosition) {
            if (!Wrapper.getPlayer().hasMoved()) event.setCancelled(true);
        } else if (event.getPacket() instanceof C03PacketPlayer.C05PacketPlayerLook) {
            if (!Wrapper.getPlayer().hasTurned()) event.setCancelled(true);
        } else if (event.getPacket() instanceof C03PacketPlayer.C06PacketPlayerPosLook) {
            if (!Wrapper.getPlayer().hasMoved() && !Wrapper.getPlayer().hasTurned()) event.setCancelled(true);
        } else if (event.getPacket() instanceof C03PacketPlayer) {
            if (Wrapper.getPlayer().ticksExisted % 20 != 0) event.setCancelled(true);
        }
    }
}
