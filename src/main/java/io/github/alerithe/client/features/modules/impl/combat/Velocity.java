package io.github.alerithe.client.features.modules.impl.combat;

import io.github.alerithe.client.events.game.EventPacket;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.features.properties.impl.IntProperty;
import io.github.alerithe.client.utilities.Wrapper;
import io.github.alerithe.events.Register;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;
import net.minecraft.util.Vec3;

public class Velocity extends Module {
    private final IntProperty verticalPercent = new IntProperty("VerticalPercent", new String[]{"vertical", "v"},
            0, 0, Integer.MAX_VALUE);
    private final IntProperty horizontalPercent = new IntProperty("HorizontalPercent", new String[]{"horizontal", "h"},
            0, 0, Integer.MAX_VALUE);

    public Velocity() {
        super("Velocity", new String[]{"antikb", "nokb"}, Type.COMBAT);

        getPropertyManager().add(verticalPercent);
        getPropertyManager().add(horizontalPercent);
    }

    @Register
    private void onPacketRead(EventPacket.Read event) {
        if (event.getPacket() instanceof S12PacketEntityVelocity) {
            S12PacketEntityVelocity packet = (S12PacketEntityVelocity) event.getPacket();
            if (packet.getEntityID() != Wrapper.getPlayer().getEntityId()) return;
            if (verticalPercent.getValue() == 0 && horizontalPercent.getValue() == 0) {
                event.setCancelled(true);
                return;
            }

            event.setPacket(new S12PacketEntityVelocity(packet.getEntityID(),
                    (packet.getMotionX() / 8000d) * (horizontalPercent.getValue() / 100d),
                    (packet.getMotionY() / 8000d) * (verticalPercent.getValue() / 100d),
                    (packet.getMotionZ() / 8000d) * (horizontalPercent.getValue() / 100d)
            ));
        }

        if (event.getPacket() instanceof S27PacketExplosion) {
            if (verticalPercent.getValue() == 0 && horizontalPercent.getValue() == 0) {
                event.setCancelled(true);
                return;
            }

            S27PacketExplosion packet = (S27PacketExplosion) event.getPacket();
            Vec3 motion = new Vec3(packet.getMotionX() * (horizontalPercent.getValue() / 100d),
                    packet.getMotionY() * (verticalPercent.getValue() / 100d),
                    packet.getMotionZ() * (horizontalPercent.getValue() / 100d));
            event.setPacket(new S27PacketExplosion(packet.getX(), packet.getY(), packet.getZ(), packet.getStrength(),
                    packet.getAffectedBlockPositions(), motion));
        }
    }
}
