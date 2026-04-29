package io.github.alerithe.client.features.modules.impl.visual.entityesp;

import io.github.alerithe.client.Client;
import io.github.alerithe.client.events.game.EventDraw;
import io.github.alerithe.client.features.modules.impl.visual.EntityESP;
import io.github.alerithe.client.utilities.EntityHelper;
import io.github.alerithe.client.utilities.GameHelper;
import io.github.alerithe.client.utilities.MathHelper;
import io.github.alerithe.client.utilities.WorldHelper;
import io.github.alerithe.client.utilities.graphics.VisualHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.opengl.GL11;

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

            double[][] vertices = getBoundingBoxVertices(entity, event.getPartialTicks());
            float[] projection = projectBoundingBox(vertices);

            projections.put(entity, projection);
        }
    }

    @Override
    public void onOverlayDraw(EventDraw.Overlay event) {
        VisualHelper.GFX_BUFFERED.begin(4);

        for (Map.Entry<Entity, float[]> projection : projections.entrySet()) {
            Entity entity = projection.getKey();
            float[] bounds = projection.getValue();

            float x = bounds[0];
            float y = bounds[1];
            float width = bounds[2] - bounds[0];
            float height = bounds[3] - bounds[1];

            VisualHelper.GFX_BUFFERED.drawBorderedSquare(x + 0.5f, y + 0.5f, width - 1f, height - 1f, 1.5f,
                    0x00000000, 0xFF000000);
            VisualHelper.GFX_BUFFERED.drawBorderedSquare(x, y, width, height, 0.5f,
                    0x00000000,
                    Client.FRIEND_MANAGER.find(entity.getName()) == null ? 0xFFFFFFFF : 0xFF00FFFF);

            if (module.showHealth.getValue()) {
                drawHealth(entity, x, y, height);
            }
        }

        VisualHelper.GFX_BUFFERED.end(GL11.GL_TRIANGLE_FAN);
    }

    private double[][] getBoundingBoxVertices(Entity entity, float partialTicks) {
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

    private float[] projectBoundingBox(double[][] vertices) {
        float[] bounds = new float[]{Float.MAX_VALUE, Float.MAX_VALUE, -1, -1};

        for (double[] vertex : vertices) {
            float[] projection = VisualHelper.project((float) vertex[0], (float) vertex[1], (float) vertex[2]);
            if (projection.length == 0
                    || projection[2] < 0f
                    || projection[2] >= 1f) {
                continue;
            }

            bounds[0] = MathHelper.min(bounds[0], projection[0]);
            bounds[1] = MathHelper.min(bounds[1], projection[1]);
            bounds[2] = MathHelper.max(bounds[2], projection[0]);
            bounds[3] = MathHelper.max(bounds[3], projection[1]);
        }

        return bounds;
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
        VisualHelper.GFX_BUFFERED.drawBorderedSquare(x - 2.5f, y + height - healthBarHeight - 0.5f, 0.5f, healthBarHeight + 1f,
                0.5f, EntityHelper.getHealthColor(living), 0xFF000000);
    }
}
