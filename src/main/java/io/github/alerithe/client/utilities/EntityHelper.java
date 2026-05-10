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

public class EntityHelper {
    private EntityHelper() {
    }

    public static LocalPlayer getUser() {
        return GameHelper.getGame().thePlayer;
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

    public static float getTotalHealth(EntityLivingBase living) {
        return living.getHealth() + living.getAbsorptionAmount();
    }

    public static int getColor(Entity entity) {
        if (Client.FRIEND_MANAGER.find(entity.getName()) != null) {
            return 0xFF00FFFF;
        }

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
