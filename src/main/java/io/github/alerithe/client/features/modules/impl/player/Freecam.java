package io.github.alerithe.client.features.modules.impl.player;

import io.github.alerithe.client.events.game.*;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.utilities.EntityHelper;
import io.github.alerithe.client.utilities.GameHelper;
import io.github.alerithe.client.utilities.WorldHelper;
import io.github.alerithe.events.impl.Subscribe;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

public class Freecam extends Module {
    private EntityOtherPlayerMP clone;

    public Freecam() {
        super("Freecam", new String[0], Type.PLAYER);
    }

    @Override
    public void onEnable() {
        clone = new EntityOtherPlayerMP(WorldHelper.getWorld(), GameHelper.getGame().getSession().getProfile());
        clone.setEntityId(-1337);
        clone.copyDataFromOld(EntityHelper.getUser());
        clone.copyLocationAndAnglesFrom(EntityHelper.getUser());
        clone.rotationYawHead = EntityHelper.getUser().rotationYawHead;
        clone.onGround = EntityHelper.getUser().onGround;
        WorldHelper.getWorld().addEntityToWorld(clone.getEntityId(), clone);
    }

    @Override
    public void onDisable() {
        EntityHelper.getUser().copyLocationAndAnglesFrom(clone);
        EntityHelper.getUser().rotationYawHead = clone.rotationYawHead;
        WorldHelper.getWorld().removeEntityFromWorld(clone.getEntityId());
        clone = null;
    }

    @Subscribe
    private void onPreUpdate(EventUpdate.Pre event) {
        EntityHelper.getUser().setSpeed(EntityHelper.getUser().isUserMoving() ? 2 : 0);

        if (EntityHelper.getUser().movementInput.jump) {
            EntityHelper.getUser().motionY = 0.75;
        } else if (EntityHelper.getUser().movementInput.sneak) {
            EntityHelper.getUser().motionY = -0.75;
        } else {
            EntityHelper.getUser().motionY = 0;
        }
    }

    @Subscribe
    private void onPacketWrite(EventPacket.Write event) {
        if (event.getPacket() instanceof C02PacketUseEntity || event.getPacket() instanceof C0BPacketEntityAction
                || event.getPacket() instanceof C07PacketPlayerDigging | event.getPacket() instanceof C0APacketAnimation
                || event.getPacket() instanceof C08PacketPlayerBlockPlacement) {
            event.cancel();
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

    @Subscribe
    private void onPacketRead(EventPacket.Read event) {
        if (!(event.getPacket() instanceof S08PacketPlayerPosLook)) return;

        S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook) event.getPacket();
        if (clone == null) return;

        clone.setPositionAndRotation(packet.getX(), packet.getY(), packet.getZ(), packet.getYaw(), packet.getPitch());
        clone.rotationYawHead = packet.getYaw();
    }

    @Subscribe
    private void onOpaqueCheck(EventOpaqueBlockCheck event) {
        event.cancel();
    }

    @Subscribe
    private void onBlockPush(EventBlockPush event) {
        event.cancel();
    }

    @Subscribe
    private void onCollision(EventBlockCollision event) {
        event.cancel();
    }
}
