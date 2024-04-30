package io.github.alerithe.client.features.modules.impl.visual.entityesp;

import io.github.alerithe.client.Client;
import io.github.alerithe.client.events.EventDraw;
import io.github.alerithe.client.features.friends.Friend;
import io.github.alerithe.client.features.modules.impl.visual.EntityESP;
import io.github.alerithe.client.utilities.MathHelper;
import io.github.alerithe.client.utilities.VisualHelper;
import io.github.alerithe.client.utilities.Wrapper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.opengl.GL11;

import javax.vecmath.Vector3d;
import javax.vecmath.Vector4f;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Rectangle extends EntityESPMode {
    public Rectangle(EntityESP module) {
        super("2D", new String[]{"rectangle", "rect"}, module);
    }

    @Override
    public void onOverlayDraw(EventDraw.Overlay event) {
        List<Entity> sorted = new ArrayList<>();
        for (Entity entity : Wrapper.getWorld().loadedEntityList) {
            if (module.qualifies(entity)) sorted.add(entity);
        }
        sorted.sort(Comparator.comparingDouble(entity -> -Wrapper.getPlayer().getDistanceSqToEntity(entity)));

        for (Entity entity : sorted) {
            GL11.glPushMatrix();
            Wrapper.getMC().entityRenderer.setupCameraTransform(event.getPartialTicks(), 0);

            double x = MathHelper.lerpd(entity.prevPosX, entity.posX, event.getPartialTicks())
                    - Wrapper.getMC().getRenderManager().viewerPosX;
            double y = MathHelper.lerpd(entity.prevPosY, entity.posY, event.getPartialTicks())
                    - Wrapper.getMC().getRenderManager().viewerPosY;
            double z = MathHelper.lerpd(entity.prevPosZ, entity.posZ, event.getPartialTicks())
                    - Wrapper.getMC().getRenderManager().viewerPosZ;

            AxisAlignedBB aabb = new AxisAlignedBB(x - entity.width / 2d, y, z - entity.width / 2d,
                    x + entity.width / 2d, y + entity.height + 0.2, z + entity.width / 2d);

            List<Vector3d> vectors = Arrays.asList(
                    new Vector3d(aabb.minX, aabb.minY, aabb.minZ), new Vector3d(aabb.minX, aabb.maxY, aabb.minZ),
                    new Vector3d(aabb.minX, aabb.maxY, aabb.maxZ), new Vector3d(aabb.minX, aabb.minY, aabb.maxZ),
                    new Vector3d(aabb.maxX, aabb.minY, aabb.minZ), new Vector3d(aabb.maxX, aabb.maxY, aabb.minZ),
                    new Vector3d(aabb.maxX, aabb.maxY, aabb.maxZ), new Vector3d(aabb.maxX, aabb.minY, aabb.maxZ)
            );

            Vector4f position = new Vector4f(Float.MAX_VALUE, Float.MAX_VALUE, -1, -1);
            for (Vector3d vector : vectors) {
                float[] projected = VisualHelper.project((float) vector.x, (float) vector.y, (float) vector.z);
                if (projected != null && !(projected[2] < 0 || projected[2] >= 1)) {
                    position.x = (float) MathHelper.min(position.x, projected[0]);
                    position.y = (float) MathHelper.min(position.y, projected[1]);
                    position.z = (float) MathHelper.max(position.z, projected[0]);
                    position.w = (float) MathHelper.max(position.w, projected[1]);
                }
            }

            Wrapper.getMC().entityRenderer.setupOverlayRendering();
            GL11.glTranslatef(position.x, position.y, 0);
            float width = position.z - position.x;
            float height = position.w - position.y;

            if (module.showNames.getValue()) {
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
                Wrapper.getFontRenderer().drawStringWithShadow(text,
                        width - (Wrapper.getFontRenderer().getStringWidth(text) / 2f),
                        -Wrapper.getFontRenderer().FONT_HEIGHT - 2, -1);

                GL11.glScaled(2, 2, 2);
            }
            VisualHelper.drawBorderedRect(0.5f, 0.5f, width - 0.5f, height - 0.5f, 1.5f, 0, 0xff000000);
            VisualHelper.drawBorderedRect(0, 0, width, height, 0.5f, 0,
                    Client.FRIEND_MANAGER.get(entity.getName()) == null ? -1 : 0xFF00FFFF);

            if (module.showHealth.getValue() && entity instanceof EntityLivingBase) {
                EntityLivingBase elb = (EntityLivingBase) entity;

                try {
                    float percent = (elb.getHealth() + elb.getAbsorptionAmount()) / elb.getMaxHealth();
                    float shownPercent = MathHelper.clamp(percent, 0f, 1f);

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

                    VisualHelper.drawBorderedRect(-2.5f, height + 0.5f, -2, height - 0.5f - (height * shownPercent), 0.5f,
                            color, 0xFF000000);
                } catch (Exception e) { // To handle Division by 0 and what not
                    e.printStackTrace();
                }
            }
            GL11.glPopMatrix();
        }
    }
}
