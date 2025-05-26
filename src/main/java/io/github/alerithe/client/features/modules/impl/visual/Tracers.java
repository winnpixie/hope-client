package io.github.alerithe.client.features.modules.impl.visual;

import io.github.alerithe.client.events.game.EventDraw;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.features.modules.impl.combat.AntiBot;
import io.github.alerithe.client.features.properties.impl.BooleanProperty;
import io.github.alerithe.client.utilities.MathHelper;
import io.github.alerithe.client.utilities.Wrapper;
import io.github.alerithe.client.utilities.graphics.VisualHelper;
import io.github.alerithe.events.CallOrder;
import io.github.alerithe.events.Register;
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

public class Tracers extends Module {
    private final BooleanProperty players = new BooleanProperty("Players", new String[0], true);
    private final BooleanProperty monsters = new BooleanProperty("Monsters", new String[]{"mobs"}, false);
    private final BooleanProperty animals = new BooleanProperty("Animals", new String[0], false);
    private final BooleanProperty passive = new BooleanProperty("Passive", new String[0], false);
    private final BooleanProperty invisibles = new BooleanProperty("Invisibles", new String[]{"invis"}, true);
    private final BooleanProperty items = new BooleanProperty("Items", new String[0], false);

    public Tracers() {
        super("Tracers", new String[0], Module.Type.VISUAL);

        getPropertyManager().add(players);
        getPropertyManager().add(monsters);
        getPropertyManager().add(animals);
        getPropertyManager().add(passive);
        getPropertyManager().add(invisibles);
        getPropertyManager().add(items);
    }

    @Register(CallOrder.FIRST)
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
                    - Wrapper.getGame().getRenderManager().viewerPosY);
            double z = (MathHelper.lerpd(entity.prevPosZ, entity.posZ, event.getPartialTicks())
                    - Wrapper.getGame().getRenderManager().viewerPosZ);

            float[] projection = VisualHelper.project((float) x, (float) y, (float) z);
            if (projection == null) continue;

            entities.add(entity);
            projections.put(entity, projection);
        }
        Wrapper.getGame().entityRenderer.setupOverlayRendering();
        GL11.glPopMatrix();
        entities.sort(Comparator.comparingDouble(entity -> Wrapper.getPlayer().getRotationsToEntity(entity)[0]));

        for (Entity entity : entities) {
            GL11.glPushMatrix();
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_LINE_SMOOTH);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glLineWidth(1f);

            int color = 0xFFFFFFFF;
            if (entity instanceof EntityLivingBase) {
                EntityLivingBase living = (EntityLivingBase) entity;
                float health = living.getHealth() + living.getAbsorptionAmount();
                float maxHealth = living.getMaxHealth();
                if (maxHealth <= 0f) maxHealth = health + 1;

                float percent = health / maxHealth;

                color = 0xFF0099FF;
                if (percent <= 0.25) {
                    color = 0xFF990000;
                } else if (percent <= 0.5) {
                    color = 0xFFFF6600;
                } else if (percent <= 0.75) {
                    color = 0xFFFFFF00;
                } else if (percent <= 1) {
                    color = 0xFF00FF00;
                }
            }

            float[] rgba = VisualHelper.toRGBAFloatArray(color, true);
            GL11.glColor3f(rgba[0], rgba[1], rgba[2]);

            float[] position = projections.get(entity);
            if (position[2] < 0f || position[2] >= 1f) {
                position[0] = VisualHelper.getDisplay().getScaledWidth() - position[0];
                position[1] = VisualHelper.getDisplay().getScaledHeight() - position[1];
            }

            GL11.glBegin(GL11.GL_LINES);
            GL11.glVertex2f(VisualHelper.getDisplay().getScaledWidth() / 2f, VisualHelper.getDisplay().getScaledHeight() / 2f);
            GL11.glVertex2f(position[0], position[1]);
            GL11.glEnd();

            GL11.glDisable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_LINE_SMOOTH);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glPopMatrix();
        }
    }

    private boolean qualifies(Entity entity) {
        return ((entity instanceof EntityPlayer && this.players.getValue() && !AntiBot.isBot((EntityPlayer) entity))
                || (entity instanceof EntityMob && this.monsters.getValue())
                || ((entity instanceof EntityAnimal || entity instanceof EntitySquid) && this.animals.getValue())
                || ((entity instanceof EntityVillager || entity instanceof EntityGolem) && this.passive.getValue())
                || (entity instanceof EntityItem && items.getValue()))
                && (!entity.isInvisible() || this.invisibles.getValue()) && entity != Wrapper.getPlayer();
    }
}
