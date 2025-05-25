package io.github.alerithe.client.features.modules.impl.miscellaneous.pingspoof;

import io.github.alerithe.client.events.game.EventPacket;
import net.minecraft.network.Packet;
import net.minecraft.network.login.server.S02PacketLoginSuccess;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.server.S00PacketKeepAlive;

public class NullPing extends SpoofMode {
    public NullPing() {
        super("NullPing", new String[]{"negping", "negativeping", "noping"});
    }

    @Override
    public void onPacketRead(EventPacket.Read event) {
        if (event.getPacket() instanceof S00PacketKeepAlive) {
            event.setCancelled(true);
        }

        if (event.getPacket() instanceof S02PacketLoginSuccess) {
            event.setCancelled(true);
            ((Packet) event.getPacket()).processPacket(event.getNetHandler());
            event.getNetworkManager().sendPacket(new C00PacketKeepAlive(0));
        }
    }
}
