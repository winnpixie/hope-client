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
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.AxisAlignedBB;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class Rectangle extends EntityESPMode {
    private final Map<Entity, float[]> projections = new TreeMap<>(
            Comparator.comparing(e -> -WorldHelper.distanceSq(e)));

    public Rectangle(EntityESP module) {
        super("2D", new String[]{"rectangle", "rect"}, module);
    }

    @Override
    public void onWorldDraw(EventDraw.World event) {
        projections.clear();

        for (Entity entity : WorldHelper.getWorld().loadedEntityList) {
            if (!module.qualifies(entity)) {
                continue;
            }

            double[][] boundingBox = getBoundingBox(entity, event.getPartialTicks());
            float[] projection = projectBoundingBox(boundingBox);

            projections.put(entity, projection);
        }
    }

    @Override
    public void onOverlayDraw(EventDraw.Overlay event) {
        for (Map.Entry<Entity, float[]> projection : projections.entrySet()) {
            Entity entity = projection.getKey();
            float[] position = projection.getValue();

            float x = position[0];
            float y = position[1];
            float width = position[2] - position[0];
            float height = position[3] - position[1];

            VisualHelper.MC_GFX.drawBorderedSquare(x + 0.5f, y + 0.5f, width - 1f, height - 1f, 1.5f, 0x00000000, 0xFF000000);
            VisualHelper.MC_GFX.drawBorderedSquare(x, y, width, height, 0.5f, 0x00000000,
                    Client.FRIEND_MANAGER.find(entity.getName()) == null ? 0xFFFFFFFF : 0xFF00FFFF);

            if (module.showNames.getValue()) {
                drawName(entity, x, y, width);
            }

            if (module.showHealth.getValue()) {
                drawHealth(entity, x, y, height);
            }
        }
    }

    private double[][] getBoundingBox(Entity entity, float partialTicks) {
        double x = (MathHelper.lerpd(entity.prevPosX, entity.posX, partialTicks)
                - GameHelper.getGame().getRenderManager().viewerPosX);
        double y = (MathHelper.lerpd(entity.prevPosY, entity.posY, partialTicks)
                - GameHelper.getGame().getRenderManager().viewerPosY);
        double z = (MathHelper.lerpd(entity.prevPosZ, entity.posZ, partialTicks)
                - GameHelper.getGame().getRenderManager().viewerPosZ);

        AxisAlignedBB aabb = new AxisAlignedBB(x - entity.width / 2.0, y, z - entity.width / 2.0,
                x + entity.width / 2.0, y + entity.height + 0.2, z + entity.width / 2.0);

        return new double[][]{
                new double[]{aabb.minX, aabb.minY, aabb.minZ},
                new double[]{aabb.minX, aabb.maxY, aabb.minZ},
                new double[]{aabb.minX, aabb.maxY, aabb.maxZ},
                new double[]{aabb.minX, aabb.minY, aabb.maxZ},
                new double[]{aabb.maxX, aabb.minY, aabb.minZ},
                new double[]{aabb.maxX, aabb.maxY, aabb.minZ},
                new double[]{aabb.maxX, aabb.maxY, aabb.maxZ},
                new double[]{aabb.maxX, aabb.minY, aabb.maxZ}
        };
    }

    private float[] projectBoundingBox(double[][] boundingBox) {
        float[] position = new float[]{Float.MAX_VALUE, Float.MAX_VALUE, -1, -1};

        for (double[] vector : boundingBox) {
            float[] projection = VisualHelper.project((float) vector[0], (float) vector[1], (float) vector[2]);
            if (projection.length == 0
                    || projection[2] < 0f
                    || projection[2] >= 1f) {
                continue;
            }

            position[0] = MathHelper.min(position[0], projection[0]);
            position[1] = MathHelper.min(position[1], projection[1]);
            position[2] = MathHelper.max(position[2], projection[0]);
            position[3] = MathHelper.max(position[3], projection[1]);
        }

        return position;
    }

    private void drawName(Entity entity, float x, float y, float width) {
        String text = entity.getDisplayName().getFormattedText();
        if (entity instanceof EntityItem) {
            text = ((EntityItem) entity).getEntityItem().getDisplayName();
        } else {
            Friend friend = Client.FRIEND_MANAGER.find(entity.getName());
            if (friend != null) {
                text = String.format("\247b%s", friend.getAliases()[0]);
            }
        }

        VisualHelper.HELVETICA.drawStringWithShadow(text,
                x + ((width - VisualHelper.HELVETICA.getStringWidth(text)) / 2f),
                y - VisualHelper.HELVETICA.getFontHeight() - 2f, -1);
    }

    private void drawHealth(Entity entity, float x, float y, float height) {
        if (!(entity instanceof EntityLivingBase)) {
            return;
        }

        EntityLivingBase living = (EntityLivingBase) entity;
        float health = living.getHealth() + living.getAbsorptionAmount();
        float maxHealth = living.getMaxHealth();
        if (maxHealth <= 0f) {
            maxHealth = health + 1;
        }

        float healthBarHeight = height * MathHelper.clamp(health / maxHealth, 0f, 1f);
        VisualHelper.MC_GFX.drawBorderedSquare(x - 2.5f, y + height - healthBarHeight - 0.5f, 0.5f, healthBarHeight + 1f,
                0.5f, EntityHelper.getHealthColor(living), 0xFF000000);
    }
}
