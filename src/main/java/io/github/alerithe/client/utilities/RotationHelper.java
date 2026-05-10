package io.github.alerithe.client.utilities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

public class RotationHelper {
    private RotationHelper() {
    }

    public static float getAngleDelta(float to, float from) {
        return (((to - (from % 360)) + 180f) % 360f) - 180f;
    }

    public static float[] getRotationToBlock(BlockPos end) {
        return getRotationToBlock(EntityHelper.getUser(),
                end);
    }

    public static float[] getRotationToBlock(Entity start, BlockPos end) {
        double startX = start.posX;
        double startY = start.posY;
        double startZ = start.posZ;

        if (start instanceof EntityLivingBase) {
            EntityLivingBase startLiving = (EntityLivingBase) start;
            startY += startLiving.getEyeHeight();
        } else {
            AxisAlignedBB box = start.getEntityBoundingBox();
            startY = (box.minY + box.maxY) / 2.0;
        }

        return getRotationToBlock(startX, startY, startZ,
                end);
    }

    public static float[] getRotationToBlock(BlockPos start, BlockPos end) {
        return getRotationToBlock(start.getX() + 0.5, start.getY() + 0.5, start.getZ() + 0.5,
                end);
    }

    public static float[] getRotationToBlock(double startX, double startY, double startZ, BlockPos end) {
        return getRotationToPosition(startX, startY, startZ,
                end.getX() + 0.5, end.getY() + 0.5, end.getZ() + 0.5);
    }

    public static float[] getRotationToEntity(Entity end) {
        return getRotationToEntity(EntityHelper.getUser(),
                end);
    }

    public static float[] getRotationToEntity(Entity start, Entity end) {
        double startX = start.posX;
        double startY = start.posY;
        double startZ = start.posZ;

        if (start instanceof EntityLivingBase) {
            EntityLivingBase living = (EntityLivingBase) start;
            startY += living.getEyeHeight();
        } else {
            AxisAlignedBB box = start.getEntityBoundingBox();
            startY = (box.minY + box.maxY) / 2.0;
        }

        return getRotationToEntity(startX, startY, startZ,
                end);
    }

    public static float[] getRotationToEntity(BlockPos start, Entity end) {
        return getRotationToEntity(start.getX() + 0.5, start.getY() + 0.5, start.getZ() + 0.5,
                end);
    }

    public static float[] getRotationToEntity(double startX, double startY, double startZ, Entity end) {
        double endX = end.posX;
        double endY = end.posY;
        double endZ = end.posZ;

        if (end instanceof EntityLivingBase) {
            EntityLivingBase living = (EntityLivingBase) end;
            endY += living.getEyeHeight();
        } else {
            AxisAlignedBB box = end.getEntityBoundingBox();
            endY = (box.minY + box.maxY) / 2.0;
        }

        return getRotationToPosition(startX, startY, startZ,
                endX, endY, endZ);
    }

    public static float[] getRotationToPosition(double endX, double endY, double endZ) {
        return getRotationToPosition(EntityHelper.getUser(),
                endX, endY, endZ);
    }

    public static float[] getRotationToPosition(Entity start, double endX, double endY, double endZ) {
        double startX = start.posX;
        double startY = start.posY;
        double startZ = start.posZ;

        if (start instanceof EntityLivingBase) {
            EntityLivingBase living = (EntityLivingBase) start;
            startY += living.getEyeHeight();
        } else {
            AxisAlignedBB box = start.getEntityBoundingBox();
            startY = (box.minY + box.maxY) / 2.0;
        }

        return getRotationToPosition(startX, startY, startZ,
                endX, endY, endZ);
    }

    public static float[] getRotationToPosition(BlockPos pos, double endX, double endY, double endZ) {
        return getRotationToPosition(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                endX, endY, endZ);
    }

    public static float[] getRotationToPosition(double startX, double startY, double startZ, double endX, double endY, double endZ) {
        double deltaX = endX - startX;
        double deltaY = endY - startY;
        double deltaZ = endZ - startZ;
        double hypotenuse = Math.sqrt((deltaX * deltaX) + (deltaZ * deltaZ));

        return new float[]{
                (float) (net.minecraft.util.MathHelper.atan2(deltaZ, deltaX) * 180d / Math.PI) - 90f,
                (float) (-(MathHelper.atan2(deltaY, hypotenuse) * 180d / Math.PI))
        };
    }
}
