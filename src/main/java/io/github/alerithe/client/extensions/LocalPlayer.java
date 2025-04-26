package io.github.alerithe.client.extensions;

import io.github.alerithe.client.events.EventBlockPush;
import io.github.alerithe.client.events.EventChat;
import io.github.alerithe.client.events.EventOpaqueCheck;
import io.github.alerithe.events.EventBus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.inventory.Slot;
import net.minecraft.stats.StatFileWriter;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import optifine.Config;

public class LocalPlayer extends EntityPlayerSP {
    public LocalPlayer(Minecraft mcIn, World worldIn, NetHandlerPlayClient netHandler, StatFileWriter statFile) {
        super(mcIn, worldIn, netHandler, statFile);
    }

    @Override
    public boolean isEntityInsideOpaqueBlock() {
        return !EventBus.dispatch(new EventOpaqueCheck()).isCancelled() && super.isEntityInsideOpaqueBlock();
    }

    @Override
    protected boolean pushOutOfBlocks(double x, double y, double z) {
        return !EventBus.dispatch(new EventBlockPush()).isCancelled() && super.pushOutOfBlocks(x, y, z);
    }

    @Override
    public void sendChatMessage(String message) {
        EventChat event = EventBus.dispatch(new EventChat(message));

        if (!event.isCancelled()) super.sendChatMessage(event.getMessage());
    }

    @Override
    public Vec3 getLook(float partialTicks) {
        if (partialTicks == 1f) {
            return this.getVectorForRotation(this.rotationPitch, this.rotationYaw);
        }

        float pitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * partialTicks;
        float yaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * partialTicks;
        return this.getVectorForRotation(pitch, yaw);
    }

    @Override
    public float getFovModifier() {
        return Config.zoomMode ? super.getFovModifier() / 4f : super.getFovModifier();
    }

    public float[] getRotationToPosition(Vec3 pos) {
        return getRotationsToEntity(new EntitySnowball(worldObj, pos.xCoord, pos.yCoord, pos.zCoord));
    }

    public float[] getRotationsToEntity(Entity entity) {
        if (entity instanceof EntityLivingBase) {
            EntityLivingBase ent = (EntityLivingBase) entity;
            return getRotationToPosition(
                    ent.posX,
                    ent.posY + ent.getEyeHeight(),
                    ent.posZ);
        }

        return getRotationToPosition(
                entity.posX,
                (entity.getEntityBoundingBox().minY + entity.getEntityBoundingBox().maxY) / 2.0,
                entity.posZ);
    }

    public float[] getRotationToPosition(double x, double y, double z) {
        double dx = x - this.posX;
        double dz = z - this.posZ;
        double dy = y - (this.posY + this.getEyeHeight());

        double hypot = MathHelper.sqrt_double(dx * dx + dz * dz);

        return new float[]{
                (float) (MathHelper.func_181159_b(dz, dx) * 180d / Math.PI) - 90f,
                (float) (-(MathHelper.func_181159_b(dy, hypot) * 180d / Math.PI))
        };
    }

    public boolean isInLiquid() {
        return isInWater() || isInLava();
    }


    public boolean isInventoryFull() {
        for (int i = 0; i < 36; i++) {
            Slot slot = inventoryContainer.inventorySlots.get(9 + i);
            if (!slot.getHasStack()) return false;
        }

        return true;
    }

    public void clickWindow(int windowId, int slot, int button, int mode) {
        mc.playerController.windowClick(windowId, slot, button, mode, this);
    }

    public boolean hasMoved() {
        return prevPosX != posX
                || prevPosY != posY
                || prevPosZ != posZ;
    }

    public boolean hasTurned() {
        return prevRotationYaw != rotationYaw
                || prevRotationPitch != rotationPitch
                || prevRotationYawHead != rotationYawHead;
    }

    public boolean isUserMoving() {
        return movementInput.moveForward != 0 || movementInput.moveStrafe != 0;
    }

    public float[] getMoveVector() {
        float yaw = rotationYawHead;
        float forward = movementInput.moveForward;
        float strafe = movementInput.moveStrafe;
        float strafeFactor = forward > 0 ? 0.5f : forward < 0 ? -0.5f : 1;

        if (strafe > 0) {
            yaw -= 90f * strafeFactor;
        } else if (strafe < 0) {
            yaw += 90f * strafeFactor;
        }

        if (forward < 0) {
            yaw += 180f;
        }

        return getLookVector(yaw);
    }

    public float[] getLookVector() {
        return getLookVector(rotationYawHead);
    }

    public float[] getLookVector(float yaw) {
        yaw *= MathHelper.deg2Rad;

        return new float[]{
                -MathHelper.sin(yaw),
                MathHelper.cos(yaw)
        };
    }

    public double getSpeed() {
        return MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);
    }

    public void setSpeed(double speed) {
        float[] vector = getMoveVector();

        motionX = vector[0] * speed;
        motionZ = vector[1] * speed;
    }
}
