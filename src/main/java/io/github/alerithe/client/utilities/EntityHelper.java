package io.github.alerithe.client.utilities;

import io.github.alerithe.client.Client;
import io.github.alerithe.client.extensions.LocalPlayer;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

public class EntityHelper {
    public static LocalPlayer getUser() {
        return GameHelper.getGame().player;
    }

    public static boolean isUser(Entity entity) {
        return entity instanceof EntityPlayerSP;
    }

    public static boolean isOtherPlayer(Entity entity) {
        return entity instanceof EntityOtherPlayerMP;
    }

    public static boolean isPlayer(Entity entity) {
        return entity instanceof EntityPlayer;
    }

    public static boolean isHostile(Entity entity) {
        return entity instanceof EntityMob
                || entity instanceof EntitySlime
                || entity instanceof EntityDragon;
    }

    public static boolean isAnimal(Entity entity) {
        return entity instanceof EntityAnimal;
    }

    public static boolean isPassive(Entity entity) {
        return entity instanceof EntityVillager
                || entity instanceof EntityGolem
                || entity instanceof EntityBat
                || entity instanceof EntitySquid;
    }

    public static boolean hasHeartBeat(Entity entity) {
        return entity.isEntityAlive();
    }

    public static float[] getRotationToBlock(BlockPos end) {
        return getRotationToBlock(getUser(),
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
        return getRotationToEntity(getUser(),
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
        return getRotationToPosition(getUser(),
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
                (float) (MathHelper.func_181159_b(deltaZ, deltaX) * 180d / Math.PI) - 90f,
                (float) (-(MathHelper.func_181159_b(deltaY, hypotenuse) * 180d / Math.PI))
        };
    }

    public static float getTotalHealth(EntityLivingBase living) {
        return living.getHealth() + living.getAbsorptionAmount();
    }

    public static int getColor(Entity entity) {
        if (Client.FRIEND_MANAGER.find(entity.getName()) != null) return 0xFF00FFFF;

        if (isPlayer(entity)) return 0xFFFF0000;
        if (isHostile(entity)) return 0xFFFF6600;
        if (isAnimal(entity)) return 0xFF00FF00;
        if (isPassive(entity)) return 0xFFFFFF00;

        return 0xFFFF00FF;
    }

    public static int getHealthColor(EntityLivingBase living) {
        float health = getTotalHealth(living);
        float maxHealth = living.getMaxHealth();
        if (maxHealth <= 0f) maxHealth = health + 1;

        float percent = health / maxHealth;

        if (percent <= 0.2) {
            return 0xFF990000;
        } else if (percent <= 0.4) {
            return 0xFFFF0000;
        } else if (percent <= 0.6) {
            return 0xFFFF6600;
        } else if (percent <= 0.8) {
            return 0xFFFFFF00;
        } else if (percent <= 1) {
            return 0xFF00FF00;
        }

        return 0xFF0099FF;
    }

    public static char getHealthColorCode(EntityLivingBase living) {
        float health = getTotalHealth(living);
        float maxHealth = living.getMaxHealth();
        if (maxHealth <= 0f) maxHealth = health + 1;

        float percent = health / maxHealth;

        if (percent <= 0.2) {
            return '4';
        } else if (percent <= 0.4) {
            return 'c';
        } else if (percent <= 0.6) {
            return '6';
        } else if (percent <= 0.8) {
            return 'e';
        } else if (percent <= 1) {
            return 'a';
        }

        return '9';
    }
}
