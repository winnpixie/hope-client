package io.github.alerithe.client.features.modules.impl.miscellaneous;

import io.github.alerithe.client.events.game.EventPacket;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.utilities.EntityHelper;
import io.github.alerithe.events.impl.Subscribe;
import net.minecraft.network.play.client.C03PacketPlayer;

public class NetPlus extends Module {
    public NetPlus() {
        super("Net+", new String[]{"netplus"}, Type.MISCELLANEOUS);
    }

    @Subscribe
    private void onPacketWrite(EventPacket.Write event) {
        if (event.getPacket() instanceof C03PacketPlayer.C04PacketPlayerPosition) {
            if (!EntityHelper.getUser().hasMoved()) event.setCancelled(true);
        } else if (event.getPacket() instanceof C03PacketPlayer.C05PacketPlayerLook) {
            if (!EntityHelper.getUser().hasTurned()) event.setCancelled(true);
        } else if (event.getPacket() instanceof C03PacketPlayer.C06PacketPlayerPosLook) {
            if (!EntityHelper.getUser().hasMoved() && !EntityHelper.getUser().hasTurned()) event.setCancelled(true);
        } else if (event.getPacket() instanceof C03PacketPlayer) {
            if (EntityHelper.getUser().ticksExisted % 20 != 0) event.setCancelled(true);
        }
    }
}
