package io.github.alerithe.client.features.modules.impl.visual;

import io.github.alerithe.client.events.EventDraw;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.features.modules.impl.combat.AntiBot;
import io.github.alerithe.client.features.properties.Property;
import io.github.alerithe.client.utilities.MathHelper;
import io.github.alerithe.client.utilities.VisualHelper;
import io.github.alerithe.client.utilities.Wrapper;
import io.github.alerithe.events.CallOrder;
import io.github.alerithe.events.Register;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Tracers extends Module {
    private final Property<Boolean> players = new Property<>("Players", new String[0], true);
    private final Property<Boolean> monsters = new Property<>("Monsters", new String[]{"mobs"}, false);
    private final Property<Boolean> animals = new Property<>("Animals", new String[0], false);
    private final Property<Boolean> passive = new Property<>("Passive", new String[0], false);
    private final Property<Boolean> invisibles = new Property<>("Invisibles", new String[]{"invis"}, true);
    private final Property<Boolean> items = new Property<>("Items", new String[0], false);

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
        List<Entity> sorted = new ArrayList<>();
        for (Entity entity : Wrapper.getWorld().loadedEntityList) {
            if (qualifies(entity)) sorted.add(entity);
        }
        sorted.sort(Comparator.comparingDouble(entity -> Wrapper.getPlayer().getRotationsToEntity(entity)[0]));

        int h = 0;
        long now = System.currentTimeMillis() / 40;
        for (Entity entity : sorted) {
            GL11.glPushMatrix();
            Wrapper.getMC().entityRenderer.setupCameraTransform(event.getPartialTicks(), 0);

            double x = MathHelper.lerpd(entity.prevPosX, entity.posX, event.getPartialTicks())
                    - Wrapper.getMC().getRenderManager().viewerPosX;
            double y = MathHelper.lerpd(entity.prevPosY, entity.posY, event.getPartialTicks())
                    - Wrapper.getMC().getRenderManager().viewerPosY;
            double z = MathHelper.lerpd(entity.prevPosZ, entity.posZ, event.getPartialTicks())
                    - Wrapper.getMC().getRenderManager().viewerPosZ;

            float[] projected = VisualHelper.project((float) x, (float) y, (float) z);
            Wrapper.getMC().entityRenderer.setupOverlayRendering();
            if (projected != null) {
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL11.glEnable(GL11.GL_LINE_SMOOTH);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glLineWidth(1f);

                float[] color = VisualHelper.toARGBFloatArray(Color.HSBtoRGB((int) ((now + h) % 255) / 255f, 1, 1), 1);
                GL11.glColor3f(color[0], color[1], color[2]);

                GL11.glBegin(GL11.GL_LINES);
                GL11.glVertex2f(VisualHelper.getDisplay().getScaledWidth() / 2f,
                        VisualHelper.getDisplay().getScaledHeight() / 2f);
                if (projected[2] < 0 || projected[2] >= 1) {
                    GL11.glVertex2f(VisualHelper.getDisplay().getScaledWidth() - projected[0],
                            VisualHelper.getDisplay().getScaledHeight() - projected[1]);
                } else {
                    GL11.glVertex2f(projected[0], projected[1]);
                }
                GL11.glEnd();

                GL11.glDisable(GL11.GL_BLEND);
                GL11.glDisable(GL11.GL_LINE_SMOOTH);
                GL11.glEnable(GL11.GL_TEXTURE_2D);
                h -= 10;
            }

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
