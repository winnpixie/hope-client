package io.github.alerithe.client.features.modules.impl.visual.entityesp;

import io.github.alerithe.client.events.game.EventDraw;
import io.github.alerithe.client.features.modules.impl.visual.EntityESP;
import io.github.alerithe.client.utilities.EntityHelper;
import io.github.alerithe.client.utilities.GameHelper;
import io.github.alerithe.client.utilities.MathHelper;
import io.github.alerithe.client.utilities.WorldHelper;
import io.github.alerithe.client.utilities.graphics.VisualHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
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
        for (Entity entity : WorldHelper.getWorld().loadedEntityList) {
            if (module.qualifies(entity)) entities.add(entity);
        }
        entities.sort(Comparator.comparingDouble(entity -> -WorldHelper.distanceSq(entity)));

        for (Entity entity : entities) {
            GlStateManager.pushMatrix();
            GlStateManager.disableDepth();
            GlStateManager.disableLighting();

            double x = MathHelper.lerpd(entity.prevPosX, entity.posX, event.getPartialTicks())
                    - GameHelper.getGame().getRenderManager().viewerPosX;
            double y = MathHelper.lerpd(entity.prevPosY, entity.posY, event.getPartialTicks())
                    - GameHelper.getGame().getRenderManager().viewerPosY;
            double z = MathHelper.lerpd(entity.prevPosZ, entity.posZ, event.getPartialTicks())
                    - GameHelper.getGame().getRenderManager().viewerPosZ;

            GlStateManager.translate(x, y, z);
            GlStateManager.rotate(-MathHelper.lerpf(entity.prevRotationYaw, entity.rotationYaw, event.getPartialTicks()),
                    0f, 1f, 0f);
            GlStateManager.translate(-x, -y, -z);

            AxisAlignedBB aabb = new AxisAlignedBB(x - entity.width / 2d, y, z - entity.width / 2d,
                    x + entity.width / 2d, y + entity.height + 0.2, z + entity.width / 2d);

            GL11.glEnable(GL11.GL_LINE_SMOOTH);
            GL11.glLineWidth(1f);
            VisualHelper.MC_GFX.drawPrism(aabb.minX, aabb.minY, aabb.minZ,
                    aabb.maxX, aabb.maxY, aabb.maxZ,
                    EntityHelper.getColor(entity));
            GL11.glDisable(GL11.GL_LINE_SMOOTH);

            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
            GlStateManager.popMatrix();
        }
    }
}
