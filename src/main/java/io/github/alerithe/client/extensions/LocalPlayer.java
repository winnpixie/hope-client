package io.github.alerithe.client.extensions;

import io.github.alerithe.client.Client;
import io.github.alerithe.client.events.game.EventBlockPush;
import io.github.alerithe.client.events.game.EventChat;
import io.github.alerithe.client.events.game.EventOpaqueBlockCheck;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
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
        return !Client.EVENT_BUS.post(new EventOpaqueBlockCheck()).isCancelled()
                && super.isEntityInsideOpaqueBlock();
    }

    @Override
    protected boolean pushOutOfBlocks(double x, double y, double z) {
        return !Client.EVENT_BUS.post(new EventBlockPush()).isCancelled()
                && super.pushOutOfBlocks(x, y, z);
    }

    @Override
    public void sendChatMessage(String message) {
        EventChat event = Client.EVENT_BUS.post(new EventChat(message));
        if (event.isCancelled()) {
            return;
        }

        super.sendChatMessage(event.getMessage());
    }

    // prplz/MouseDelayFix
    @Override
    public Vec3 getLook(float partialTicks) {
        if (partialTicks == 1f) {
            return this.getVectorForRotation(rotationPitch, rotationYaw);
        }

        float pitch = prevRotationPitch + (rotationPitch - prevRotationPitch) * partialTicks;
        float yaw = prevRotationYaw + (rotationYaw - prevRotationYaw) * partialTicks;
        return this.getVectorForRotation(pitch, yaw);
    }

    @Override
    public float getFovModifier() {
        float fovModifier = super.getFovModifier();
        if (!Config.zoomMode) {
            return fovModifier;
        }

        return fovModifier / 4f;
    }

    public boolean isInLiquid() {
        return isInWater()
                || isInLava();
    }

    public boolean isInventoryFull() {
        for (int i = 0; i < 36; i++) {
            Slot slot = inventoryContainer.inventorySlots.get(9 + i);
            if (!slot.getHasStack()) {
                return false;
            }
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
        return movementInput.moveForward != 0f
                || movementInput.moveStrafe != 0f;
    }

    public float[] getMoveVector() {
        float yaw = rotationYawHead;
        float forward = movementInput.moveForward;
        float strafe = movementInput.moveStrafe;

        float factor = 1f;
        if (forward > 0f) {
            factor = 0.5f;
        } else if (forward < 0f) {
            factor = -0.5f;
        }

        if (strafe > 0f) {
            yaw -= 90f * factor;
        } else if (strafe < 0f) {
            yaw += 90f * factor;
        }

        if (forward < 0f) {
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

        this.motionX = vector[0] * speed;
        this.motionZ = vector[1] * speed;
    }
}
