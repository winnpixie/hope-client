package io.github.alerithe.client.features.modules.impl.visual.entityesp;

import io.github.alerithe.client.Client;
import io.github.alerithe.client.events.game.EventDraw;
import io.github.alerithe.client.features.friends.Friend;
import io.github.alerithe.client.features.modules.impl.visual.EntityESP;
import io.github.alerithe.client.utilities.EntityHelper;
import io.github.alerithe.client.utilities.GameHelper;
import io.github.alerithe.client.utilities.MathHelper;
import io.github.alerithe.client.utilities.WorldHelper;
import io.github.alerithe.client.utilities.graphics.VisualHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.AxisAlignedBB;

import java.util.*;

public class Rectangle extends EntityESPMode {
    public Rectangle(EntityESP module) {
        super("2D", new String[]{"rectangle", "rect"}, module);
    }

    @Override
    public void onOverlayDraw(EventDraw.Overlay event) {
        List<Entity> entities = new ArrayList<>();
        Map<Entity, float[]> projections = new HashMap<>();

        GlStateManager.pushMatrix();
        GameHelper.getGame().entityRenderer.setupCameraTransform(event.getPartialTicks(), 0);
        for (Entity entity : WorldHelper.getWorld().loadedEntityList) {
            if (!module.qualifies(entity)) continue;

            double x = (MathHelper.lerpd(entity.prevPosX, entity.posX, event.getPartialTicks())
                    - GameHelper.getGame().getRenderManager().viewerPosX);
            double y = (MathHelper.lerpd(entity.prevPosY, entity.posY, event.getPartialTicks())
                    - GameHelper.getGame().getRenderManager().viewerPosY);
            double z = (MathHelper.lerpd(entity.prevPosZ, entity.posZ, event.getPartialTicks())
                    - GameHelper.getGame().getRenderManager().viewerPosZ);

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

        GameHelper.getGame().entityRenderer.setupOverlayRendering();
        GlStateManager.popMatrix();
        entities.sort(Comparator.comparingDouble(entity -> -WorldHelper.distanceSq(entity)));

        for (Entity entity : entities) {
            float[] position = projections.get(entity);
            float x = position[0];
            float y = position[1];
            float width = position[2] - position[0];
            float height = position[3] - position[1];

            if (module.showNames.getValue()) {
                GlStateManager.pushMatrix();
                GlStateManager.translate(position[0], position[1], 0f);
                GlStateManager.scale(0.5f, 0.5f, 0.5f);

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
                GlStateManager.popMatrix();
            }

            VisualHelper.MC_GFX.drawBorderedSquare(x + 0.5f, y + 0.5f, width - 1f, height - 1f, 1.5f, 0x00000000, 0xFF000000);
            VisualHelper.MC_GFX.drawBorderedSquare(x, y, width, height, 0.5f, 0x00000000,
                    Client.FRIEND_MANAGER.find(entity.getName()) == null ? 0xFFFFFFFF : 0xFF00FFFF);

            if (module.showHealth.getValue() && entity instanceof EntityLivingBase) {
                EntityLivingBase living = (EntityLivingBase) entity;
                float health = living.getHealth() + living.getAbsorptionAmount();
                float maxHealth = living.getMaxHealth();
                if (maxHealth <= 0f) maxHealth = health + 1;

                float healthBarHeight = height * MathHelper.clamp(health / maxHealth, 0f, 1f);
                VisualHelper.MC_GFX.drawBorderedSquare(x - 2.5f, y + height - healthBarHeight - 0.5f, 0.5f, healthBarHeight + 1f,
                        0.5f, EntityHelper.getHealthColor(living), 0xFF000000);
            }
        }
    }
}
