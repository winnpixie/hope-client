package io.github.alerithe.client.features.modules.impl.visual;

import io.github.alerithe.client.Client;
import io.github.alerithe.client.events.game.EventDraw;
import io.github.alerithe.client.features.friends.Friend;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.features.modules.impl.combat.AntiBot;
import io.github.alerithe.client.features.properties.impl.BooleanProperty;
import io.github.alerithe.client.utilities.MathHelper;
import io.github.alerithe.client.utilities.graphics.VisualHelper;
import io.github.alerithe.client.utilities.Wrapper;
import io.github.alerithe.events.Register;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

import java.util.*;

public class NameTags extends Module {
    private final BooleanProperty players = new BooleanProperty("Players", new String[0], true);
    private final BooleanProperty monsters = new BooleanProperty("Monsters", new String[]{"mobs"}, false);
    private final BooleanProperty animals = new BooleanProperty("Animals", new String[0], false);
    private final BooleanProperty passive = new BooleanProperty("Passive", new String[0], false);
    private final BooleanProperty invisibles = new BooleanProperty("Invisibles", new String[]{"invis"}, true);
    private final BooleanProperty items = new BooleanProperty("Items", new String[0], false);
    private final BooleanProperty ping = new BooleanProperty("ShowPing", new String[0], true);
    private final BooleanProperty showHealth = new BooleanProperty("ShowHealth", new String[]{"hp"}, true);

    public NameTags() {
        super("NameTags", new String[]{"tags"}, Type.VISUAL);

        getPropertyManager().add(players);
        getPropertyManager().add(monsters);
        getPropertyManager().add(animals);
        getPropertyManager().add(passive);
        getPropertyManager().add(invisibles);
        getPropertyManager().add(items);
        getPropertyManager().add(ping);
        getPropertyManager().add(showHealth);
    }

    @Register
    private void onOverlayDraw(EventDraw.Overlay event) {
        List<Entity> entities = new ArrayList<>();
        Map<Entity, float[]> projections = new HashMap<>();

        GL11.glPushMatrix();
        Wrapper.getGame().entityRenderer.setupCameraTransform(event.getPartialTicks(), 0);
        for (Entity entity : Wrapper.getWorld().loadedEntityList) {
            if (!qualifies(entity)) continue;

            double x = (MathHelper.lerpd(entity.prevPosX, entity.posX, event.getPartialTicks())
                    - Wrapper.getGame().getRenderManager().viewerPosX);
            double y = (MathHelper.lerpd(entity.prevPosY, entity.posY, event.getPartialTicks())
                    + entity.height + 0.2 - Wrapper.getGame().getRenderManager().viewerPosY);
            double z = (MathHelper.lerpd(entity.prevPosZ, entity.posZ, event.getPartialTicks())
                    - Wrapper.getGame().getRenderManager().viewerPosZ);

            float[] projection = VisualHelper.project((float) x, (float) y, (float) z);

            if (projection == null) continue;
            if (projection[2] < 0f) continue;
            if (projection[2] >= 1f) continue;

            entities.add(entity);
            projections.put(entity, projection);
        }
        Wrapper.getGame().entityRenderer.setupOverlayRendering();
        GL11.glPopMatrix();
        entities.sort(Comparator.comparingDouble(entity -> -Wrapper.getPlayer().getDistanceSqToEntity(entity)));

        for (Entity entity : entities) {
            String text = entity.getDisplayName().getFormattedText();
            if (entity instanceof EntityItem) {
                text = ((EntityItem) entity).getEntityItem().getDisplayName();
            } else {
                Friend friend = Client.FRIEND_MANAGER.find(entity.getName());
                if (friend != null) {
                    text = String.format("\247b%s", friend.getAliases()[0]);
                }
            }

            if (ping.getValue() && entity instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) entity;
                if (player.getGameProfile() != null) {
                    NetworkPlayerInfo npi = Wrapper.getNetInfo(player.getGameProfile().getId());
                    if (npi != null) {
                        text = String.format("\247a%dms\247r %s", npi.getResponseTime(), text);
                    }
                }
            }

            if (showHealth.getValue() && entity instanceof EntityLivingBase) {
                EntityLivingBase elb = (EntityLivingBase) entity;
                float health = elb.getHealth();
                float maxHealth = elb.getMaxHealth();
                if (maxHealth <= 0f) maxHealth = health + 1;

                float percent = health / maxHealth;

                char color = '9';
                if (percent <= 0.25) {
                    color = 'c';
                } else if (percent <= 0.5) {
                    color = '6';
                } else if (percent <= 0.75) {
                    color = 'e';
                } else if (percent <= 1) {
                    color = 'a';
                }

                text += String.format(" \247%s%dHP", color, MathHelper.ceil(elb.getHealth() + elb.getAbsorptionAmount()));
            }

            float[] position = projections.get(entity);
            GL11.glPushMatrix();
            GL11.glTranslatef(position[0], position[1], 0);
            GL11.glScalef(0.5f, 0.5f, 1f);
            float width = VisualHelper.MC_FONT.getStringWidth(text);

            // TODO: Convert to drawSquare
            VisualHelper.drawRect(-width / 2f - 2, -1, width / 2f + 2, 9, 0x77000000);
            VisualHelper.MC_FONT.drawStringWithShadow(text, -width / 2f, 0, -1);
            GL11.glPopMatrix();
        }
    }

    @Register
    private void onTagDraw(EventDraw.Tag event) {
        event.setCancelled(qualifies(event.getEntity()));
    }

    private boolean qualifies(Entity entity) {
        return ((entity instanceof EntityPlayer && this.players.getValue() && !AntiBot.isBot((EntityPlayer) entity))
                || (entity instanceof EntityMob && this.monsters.getValue())
                || ((entity instanceof EntityAnimal || entity instanceof EntitySquid) && this.animals.getValue())
                || ((entity instanceof EntityVillager || entity instanceof EntityGolem) && this.passive.getValue())
                || (entity instanceof EntityItem && items.getValue()))
                && (!entity.isInvisible() || this.invisibles.getValue()) && VisualHelper.isInView(entity)
                && entity != Wrapper.getPlayer();
    }
}
