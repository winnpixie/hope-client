package io.github.alerithe.client.features.modules.impl.visual.entityesp;

import io.github.alerithe.client.Client;
import io.github.alerithe.client.events.game.EventDraw;
import io.github.alerithe.client.features.friends.Friend;
import io.github.alerithe.client.features.modules.impl.visual.EntityESP;
import io.github.alerithe.client.utilities.MathHelper;
import io.github.alerithe.client.utilities.graphics.VisualHelper;
import io.github.alerithe.client.utilities.Wrapper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.opengl.GL11;

import java.util.*;

public class Rectangle extends EntityESPMode {
    public Rectangle(EntityESP module) {
        super("2D", new String[]{"rectangle", "rect"}, module);
    }

    @Override
    public void onOverlayDraw(EventDraw.Overlay event) {
        List<Entity> entities = new ArrayList<>();
        Map<Entity, float[]> projections = new HashMap<>();

        GL11.glPushMatrix();
        Wrapper.getGame().entityRenderer.setupCameraTransform(event.getPartialTicks(), 0);
        for (Entity entity : Wrapper.getWorld().loadedEntityList) {
            if (!module.qualifies(entity)) continue;

            double x = (MathHelper.lerpd(entity.prevPosX, entity.posX, event.getPartialTicks())
                    - Wrapper.getGame().getRenderManager().viewerPosX);
            double y = (MathHelper.lerpd(entity.prevPosY, entity.posY, event.getPartialTicks())
                    - Wrapper.getGame().getRenderManager().viewerPosY);
            double z = (MathHelper.lerpd(entity.prevPosZ, entity.posZ, event.getPartialTicks())
                    - Wrapper.getGame().getRenderManager().viewerPosZ);

            AxisAlignedBB aabb = new AxisAlignedBB(x - entity.width / 2d, y, z - entity.width / 2d,
                    x + entity.width / 2d, y + entity.height + 0.2, z + entity.width / 2d);

            List<double[]> vectors = Arrays.asList(
                    new double[]{aabb.minX, aabb.minY, aabb.minZ}, new double[]{aabb.minX, aabb.maxY, aabb.minZ},
                    new double[]{aabb.minX, aabb.maxY, aabb.maxZ}, new double[]{aabb.minX, aabb.minY, aabb.maxZ},
                    new double[]{aabb.maxX, aabb.minY, aabb.minZ}, new double[]{aabb.maxX, aabb.maxY, aabb.minZ},
                    new double[]{aabb.maxX, aabb.maxY, aabb.maxZ}, new double[]{aabb.maxX, aabb.minY, aabb.maxZ}
            );

            float[] position = new float[]{Float.MAX_VALUE, Float.MAX_VALUE, -1, -1};
            for (double[] vector : vectors) {
                float[] projection = VisualHelper.project((float) vector[0], (float) vector[1], (float) vector[2]);
                if (projection == null) continue;
                if (projection[2] < 0f) continue;
                if (projection[2] >= 1f) continue;

                position[0] = MathHelper.min(position[0], projection[0]);
                position[1] = MathHelper.min(position[1], projection[1]);
                position[2] = MathHelper.max(position[2], projection[0]);
                position[3] = MathHelper.max(position[3], projection[1]);
            }

            entities.add(entity);
            projections.put(entity, position);
        }
        Wrapper.getGame().entityRenderer.setupOverlayRendering();
        GL11.glPopMatrix();
        entities.sort(Comparator.comparingDouble(entity -> -Wrapper.getPlayer().getDistanceSqToEntity(entity)));

        for (Entity entity : entities) {
            float[] position = projections.get(entity);
            float width = position[2] - position[0];
            float height = position[3] - position[1];

            GL11.glPushMatrix();
            GL11.glTranslatef(position[0], position[1], 0);

            if (module.showNames.getValue()) {
                GL11.glScaled(0.5, 0.5, 0.5);

                String text = entity.getDisplayName().getFormattedText();
                if (entity instanceof EntityItem) {
                    text = ((EntityItem) entity).getEntityItem().getDisplayName();
                } else {
                    Friend friend = Client.FRIEND_MANAGER.find(entity.getName());
                    if (friend != null) {
                        text = String.format("\247b%s", friend.getAliases()[0]);
                    }
                }

                VisualHelper.MC_FONT.drawStringWithShadow(text,
                        width - (VisualHelper.MC_FONT.getStringWidth(text) / 2f),
                        -VisualHelper.MC_FONT.getFontHeight() - 2, -1);
                GL11.glScaled(2, 2, 2);
            }

            VisualHelper.drawBorderedRect(0.5f, 0.5f, width - 0.5f, height - 0.5f, 1.5f, 0, 0xff000000);
            VisualHelper.drawBorderedRect(0, 0, width, height, 0.5f, 0,
                    Client.FRIEND_MANAGER.find(entity.getName()) == null ? -1 : 0xFF00FFFF);

            if (module.showHealth.getValue() && entity instanceof EntityLivingBase) {
                EntityLivingBase elb = (EntityLivingBase) entity;
                float health = elb.getHealth() + elb.getAbsorptionAmount();
                float maxHealth = elb.getMaxHealth();
                if (maxHealth <= 0f) maxHealth = health + 1;

                float percent = health / maxHealth;

                int color = 0xFF0099FF;
                if (percent <= 0.25) {
                    color = 0xFF990000;
                } else if (percent <= 0.5) {
                    color = 0xFFFF6600;
                } else if (percent <= 0.75) {
                    color = 0xFFFFFF00;
                } else if (percent <= 1) {
                    color = 0xFF00FF00;
                }

                VisualHelper.drawBorderedRect(-2.5f, height + 0.5f, -2, height - 0.5f - (height * MathHelper.clamp(percent, 0f, 1f)),
                        0.5f, color, 0xFF000000);
            }

            GL11.glPopMatrix();
        }
    }
}
