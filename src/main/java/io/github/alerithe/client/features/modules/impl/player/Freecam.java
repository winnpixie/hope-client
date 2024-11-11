package io.github.alerithe.client.features.modules.impl.player;

import io.github.alerithe.client.events.*;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.utilities.Wrapper;
import io.github.alerithe.events.Register;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

public class Freecam extends Module {
    private EntityOtherPlayerMP clone;

    public Freecam() {
        super("Freecam", new String[0], Type.PLAYER);
    }

    @Override
    public void enable() {
        clone = new EntityOtherPlayerMP(Wrapper.getWorld(), Wrapper.getGame().getSession().getProfile());
        clone.setEntityId(-1337);
        clone.copyDataFromOld(Wrapper.getPlayer());
        clone.copyLocationAndAnglesFrom(Wrapper.getPlayer());
        clone.rotationYawHead = Wrapper.getPlayer().rotationYawHead;
        clone.onGround = Wrapper.getPlayer().onGround;
        Wrapper.getWorld().addEntityToWorld(clone.getEntityId(), clone);

        super.enable();
    }

    @Override
    public void disable() {
        super.disable();

        Wrapper.getPlayer().copyLocationAndAnglesFrom(clone);
        Wrapper.getPlayer().rotationYawHead = clone.rotationYawHead;
        Wrapper.getWorld().removeEntityFromWorld(clone.getEntityId());
        clone = null;
    }

    @Register
    private void onPreUpdate(EventUpdate.Pre event) {
        Wrapper.getPlayer().setSpeed(Wrapper.getPlayer().isUserMoving() ? 2 : 0);

        if (Wrapper.getPlayer().movementInput.jump) {
            Wrapper.getPlayer().motionY = 0.75;
        } else if (Wrapper.getPlayer().movementInput.sneak) {
            Wrapper.getPlayer().motionY = -0.75;
        } else {
            Wrapper.getPlayer().motionY = 0;
        }
    }

    @Register
    private void onPacketWrite(EventPacket.Write event) {
        if (event.getPacket() instanceof C02PacketUseEntity || event.getPacket() instanceof C0BPacketEntityAction
                || event.getPacket() instanceof C07PacketPlayerDigging | event.getPacket() instanceof C0APacketAnimation
                || event.getPacket() instanceof C08PacketPlayerBlockPlacement) {
            event.setCancelled(true);
        }

        if (event.getPacket() instanceof C03PacketPlayer.C06PacketPlayerPosLook) {
            event.setPacket(new C03PacketPlayer.C06PacketPlayerPosLook(clone.posX, clone.posY, clone.posZ,
                    clone.rotationYaw, clone.rotationPitch,
                    clone.onGround));
        } else if (event.getPacket() instanceof C03PacketPlayer.C05PacketPlayerLook) {
            event.setPacket(new C03PacketPlayer.C05PacketPlayerLook(clone.rotationYaw, clone.rotationPitch,
                    clone.onGround));
        } else if (event.getPacket() instanceof C03PacketPlayer.C04PacketPlayerPosition) {
            event.setPacket(new C03PacketPlayer.C04PacketPlayerPosition(clone.posX, clone.posY, clone.posZ,
                    clone.onGround));
        } else if (event.getPacket() instanceof C03PacketPlayer) {
            event.setPacket(new C03PacketPlayer(clone.onGround));
        }
    }

    @Register
    private void onPacketRead(EventPacket.Read event) {
        if (!(event.getPacket() instanceof S08PacketPlayerPosLook)) return;

        S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook) event.getPacket();
        if (clone == null) return;

        clone.setPositionAndRotation(packet.getX(), packet.getY(), packet.getZ(), packet.getYaw(), packet.getPitch());
        clone.rotationYawHead = packet.getYaw();
    }

    @Register
    private void onOpaqueCheck(EventOpaqueCheck event) {
        event.setCancelled(true);
    }

    @Register
    private void onBlockPush(EventBlockPush event) {
        event.setCancelled(true);
    }

    @Register
    private void onCollision(EventBlockCollision event) {
        event.setCancelled(true);
    }
}
