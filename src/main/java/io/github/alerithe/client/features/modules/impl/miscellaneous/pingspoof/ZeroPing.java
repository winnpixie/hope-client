package io.github.alerithe.client.features.modules.impl.miscellaneous.pingspoof;

import io.github.alerithe.client.events.game.EventPacket;
import net.minecraft.network.play.server.S00PacketKeepAlive;

public class ZeroPing extends SpoofMode {
    public ZeroPing() {
        super("0Ping", new String[]{"zeroping"});
    }

    @Override
    public void onPacketRead(EventPacket.Read event) {
        if (!(event.getPacket() instanceof S00PacketKeepAlive)) return;

        event.setCancelled(true);
    }
}
