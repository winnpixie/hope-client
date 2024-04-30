package io.github.alerithe.client.features.modules.impl.visual;

import io.github.alerithe.client.Client;
import io.github.alerithe.client.events.EventDraw;
import io.github.alerithe.client.features.friends.Friend;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.features.modules.impl.combat.AntiBot;
import io.github.alerithe.client.features.properties.Property;
import io.github.alerithe.client.utilities.MathHelper;
import io.github.alerithe.client.utilities.VisualHelper;
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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class NameTags extends Module {
    private final Property<Boolean> players = new Property<>("Players", new String[0], true);
    private final Property<Boolean> monsters = new Property<>("Monsters", new String[]{"mobs"}, false);
    private final Property<Boolean> animals = new Property<>("Animals", new String[0], false);
    private final Property<Boolean> passive = new Property<>("Passive", new String[0], false);
    private final Property<Boolean> invisibles = new Property<>("Invisibles", new String[]{"invis"}, true);
    private final Property<Boolean> items = new Property<>("Items", new String[0], false);
    private final Property<Boolean> ping = new Property<>("Ping", new String[0], true);
    private final Property<Boolean> health = new Property<>("Health", new String[]{"hp"}, true);

    public NameTags() {
        super("NameTags", new String[]{"tags"}, Type.VISUAL);

        getPropertyManager().add(players);
        getPropertyManager().add(monsters);
        getPropertyManager().add(animals);
        getPropertyManager().add(passive);
        getPropertyManager().add(invisibles);
        getPropertyManager().add(items);
        getPropertyManager().add(ping);
        getPropertyManager().add(health);
    }

    @Register
    private void onOverlayDraw(EventDraw.Overlay event) {
        List<Entity> sorted = new ArrayList<>();
        for (Entity entity : Wrapper.getWorld().loadedEntityList) {
            if (qualifies(entity)) sorted.add(entity);
        }
        sorted.sort(Comparator.comparingDouble(entity -> -Wrapper.getPlayer().getDistanceSqToEntity(entity)));

        for (Entity entity : sorted) {
            GL11.glPushMatrix();
            Wrapper.getMC().entityRenderer.setupCameraTransform(event.getPartialTicks(), 0);

            double x = (MathHelper.lerpd(entity.prevPosX, entity.posX, event.getPartialTicks())
                    - Wrapper.getMC().getRenderManager().viewerPosX);
            double y = (MathHelper.lerpd(entity.prevPosY, entity.posY, event.getPartialTicks())
                    + entity.height + 0.2 - Wrapper.getMC().getRenderManager().viewerPosY);
            double z = (MathHelper.lerpd(entity.prevPosZ, entity.posZ, event.getPartialTicks())
                    - Wrapper.getMC().getRenderManager().viewerPosZ);

            float[] vector = VisualHelper.project((float) x, (float) y, (float) z);
            Wrapper.getMC().entityRenderer.setupOverlayRendering();
            if (vector != null && !(vector[2] < 0 || vector[2] >= 1)) {
                GL11.glTranslatef(vector[0], vector[1], 0);
                GL11.glScaled(0.5, 0.5, 0.5);

                String text = entity.getDisplayName().getFormattedText();
                if (entity instanceof EntityItem) {
                    text = ((EntityItem) entity).getEntityItem().getDisplayName();
                } else {
                    Friend friend = Client.FRIEND_MANAGER.get(entity.getName());
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

                if (health.getValue() && entity instanceof EntityLivingBase) {
                    EntityLivingBase elb = (EntityLivingBase) entity;
                    float percent = (elb.getHealth() + elb.getAbsorptionAmount()) / elb.getMaxHealth();

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

                int width = Wrapper.getFontRenderer().getStringWidth(text);
                VisualHelper.drawRect(-width / 2f - 2, -1, width / 2f + 2, 9, 0x77000000);
                Wrapper.getFontRenderer().drawStringWithShadow(text, -width / 2f, 0, -1);
            }

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
