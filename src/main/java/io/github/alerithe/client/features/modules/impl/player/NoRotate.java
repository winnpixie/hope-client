package io.github.alerithe.client.features.modules.impl.player;

import io.github.alerithe.client.events.bus.Subscribe;
import io.github.alerithe.client.events.game.EventPacket;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.utilities.EntityHelper;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S14PacketEntity;
import net.minecraft.network.play.server.S18PacketEntityTeleport;

public class NoRotate extends Module {
    public NoRotate() {
        super("NoRotate", new String[0], Type.PLAYER);
    }

    @Subscribe
    public void onPacketRead(EventPacket.Read event) {
        if (EntityHelper.getUser() == null) return;

        if (event.getPacket() instanceof S08PacketPlayerPosLook) {
            S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook) event.getPacket();

            event.setPacket(new S08PacketPlayerPosLook(
                    packet.getX(), packet.getY(), packet.getZ(),
                    EntityHelper.getUser().rotationYaw,
                    EntityHelper.getUser().rotationPitch,
                    packet.func_179834_f()));
        } else if (event.getPacket() instanceof S14PacketEntity.S17PacketEntityLookMove) {
            S14PacketEntity.S17PacketEntityLookMove packet = (S14PacketEntity.S17PacketEntityLookMove) event.getPacket();
            if (packet.entityId != EntityHelper.getUser().getEntityId()) return;

            event.setPacket(new S14PacketEntity.S17PacketEntityLookMove(packet.entityId,
                    packet.func_149062_c(), packet.func_149061_d(), packet.func_149064_e(),
                    (byte) (EntityHelper.getUser().rotationYaw * 256f / 360f),
                    (byte) (EntityHelper.getUser().rotationPitch * 256f / 360f),
                    packet.getOnGround()));
        } else if (event.getPacket() instanceof S14PacketEntity.S16PacketEntityLook) {
            S14PacketEntity.S16PacketEntityLook packet = (S14PacketEntity.S16PacketEntityLook) event.getPacket();
            if (packet.entityId != EntityHelper.getUser().getEntityId()) return;

            event.setPacket(new S14PacketEntity.S16PacketEntityLook(packet.entityId,
                    (byte) (EntityHelper.getUser().rotationYaw * 256f / 360f),
                    (byte) (EntityHelper.getUser().rotationPitch * 256f / 360f),
                    packet.getOnGround()));
        } else if (event.getPacket() instanceof S18PacketEntityTeleport) {
            S18PacketEntityTeleport packet = (S18PacketEntityTeleport) event.getPacket();
            if (packet.getEntityId() != EntityHelper.getUser().getEntityId()) return;

            event.setPacket(new S18PacketEntityTeleport(packet.getEntityId(),
                    packet.getX(), packet.getY(), packet.getZ(),
                    (byte) (EntityHelper.getUser().rotationYaw * 256f / 360f),
                    (byte) (EntityHelper.getUser().rotationPitch * 256f / 360f),
                    packet.getOnGround()));
        }
    }
}
