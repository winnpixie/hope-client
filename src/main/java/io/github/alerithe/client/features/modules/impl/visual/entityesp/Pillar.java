package io.github.alerithe.client.features.modules.impl.visual.entityesp;

import io.github.alerithe.client.Client;
import io.github.alerithe.client.events.EventDraw;
import io.github.alerithe.client.features.modules.impl.visual.EntityESP;
import io.github.alerithe.client.utilities.MathHelper;
import io.github.alerithe.client.utilities.VisualHelper;
import io.github.alerithe.client.utilities.Wrapper;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Pillar extends EntityESPMode {
    public Pillar(EntityESP module) {
        super("3D", new String[]{"pillar"}, module);
    }

    @Override
    public void onWorldDraw(EventDraw.World event) {
        List<Entity> entities = new ArrayList<>();
        for (Entity entity : Wrapper.getWorld().loadedEntityList) {
            if (module.qualifies(entity)) entities.add(entity);
        }
        entities.sort(Comparator.comparingDouble(entity -> -Wrapper.getPlayer().getDistanceSqToEntity(entity)));

        for (Entity entity : entities) {
            GL11.glPushMatrix();
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_LINE_SMOOTH);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glLineWidth(1f);

            double x = MathHelper.lerpd(entity.prevPosX, entity.posX, event.getPartialTicks())
                    - Wrapper.getMC().getRenderManager().viewerPosX;
            double y = MathHelper.lerpd(entity.prevPosY, entity.posY, event.getPartialTicks())
                    - Wrapper.getMC().getRenderManager().viewerPosY;
            double z = MathHelper.lerpd(entity.prevPosZ, entity.posZ, event.getPartialTicks())
                    - Wrapper.getMC().getRenderManager().viewerPosZ;

            GL11.glTranslated(x, y, z);
            GL11.glRotatef(-MathHelper.lerpf(entity.prevRotationYaw, entity.rotationYaw, event.getPartialTicks()), 0, 1, 0);
            GL11.glTranslated(-x, -y, -z);

            AxisAlignedBB aabb = new AxisAlignedBB(x - entity.width / 2d, y, z - entity.width / 2d,
                    x + entity.width / 2d, y + entity.height + 0.2, z + entity.width / 2d);

            int color = 0xFFFFFFFF;
            if (module.showHealth.getValue() && entity instanceof EntityLivingBase) {
                EntityLivingBase elb = (EntityLivingBase) entity;
                float health = elb.getHealth() + elb.getAbsorptionAmount();
                if (health > 0f) {
                    float percent = health / elb.getMaxHealth();

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
            }

            if (Client.FRIEND_MANAGER.get(entity.getName()) != null) color = 0xFF00FFFF;

            float[] rgba = VisualHelper.toARGBFloatArray(color, -1f);
            GL11.glColor4f(rgba[0], rgba[1], rgba[2], rgba[3]);

            RenderGlobal.func_181561_a(aabb);

            GL11.glDisable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_LINE_SMOOTH);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glPopMatrix();
        }
    }
}
