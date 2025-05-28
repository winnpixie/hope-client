package io.github.alerithe.client.features.modules.impl.visual;

import io.github.alerithe.client.events.game.EventDraw;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.features.modules.impl.combat.AntiBot;
import io.github.alerithe.client.features.properties.impl.BooleanProperty;
import io.github.alerithe.client.utilities.EntityHelper;
import io.github.alerithe.client.utilities.GameHelper;
import io.github.alerithe.client.utilities.MathHelper;
import io.github.alerithe.client.utilities.WorldHelper;
import io.github.alerithe.client.utilities.graphics.VisualHelper;
import io.github.alerithe.events.impl.Subscribe;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

import java.util.*;

public class Tracers extends Module {
    private final BooleanProperty players = new BooleanProperty("Players", new String[0], true);
    private final BooleanProperty hostiles = new BooleanProperty("Hostiles", new String[]{"monsters", "mobs"}, false);
    private final BooleanProperty animals = new BooleanProperty("Animals", new String[0], false);
    private final BooleanProperty passive = new BooleanProperty("Passive", new String[0], false);
    private final BooleanProperty invisibles = new BooleanProperty("Invisibles", new String[]{"invis"}, true);
    private final BooleanProperty items = new BooleanProperty("Items", new String[0], false);

    public Tracers() {
        super("Tracers", new String[0], Module.Type.VISUAL);

        getPropertyManager().add(players);
        getPropertyManager().add(hostiles);
        getPropertyManager().add(animals);
        getPropertyManager().add(passive);
        getPropertyManager().add(invisibles);
        getPropertyManager().add(items);
    }

    @Subscribe
    private void onOverlayDraw(EventDraw.Overlay event) {
        List<Entity> entities = new ArrayList<>();
        Map<Entity, float[]> projections = new HashMap<>();

        GlStateManager.pushMatrix();
        GameHelper.getGame().entityRenderer.setupCameraTransform(event.getPartialTicks(), 0);
        for (Entity entity : WorldHelper.getWorld().loadedEntityList) {
            if (!qualifies(entity)) continue;

            double x = (MathHelper.lerpd(entity.prevPosX, entity.posX, event.getPartialTicks())
                    - GameHelper.getGame().getRenderManager().viewerPosX);
            double y = (MathHelper.lerpd(entity.prevPosY, entity.posY, event.getPartialTicks())
                    - GameHelper.getGame().getRenderManager().viewerPosY);
            double z = (MathHelper.lerpd(entity.prevPosZ, entity.posZ, event.getPartialTicks())
                    - GameHelper.getGame().getRenderManager().viewerPosZ);

            float[] projection = VisualHelper.project((float) x, (float) y, (float) z);
            if (projection == null) continue;

            entities.add(entity);
            projections.put(entity, projection);
        }
        GameHelper.getGame().entityRenderer.setupOverlayRendering();
        GlStateManager.popMatrix();
        entities.sort(Comparator.comparingDouble(entity -> EntityHelper.getRotationToEntity(entity)[0]));

        ScaledResolution display = VisualHelper.getDisplay();
        float windowWidth = display.getScaledWidth();
        float windowHeight = display.getScaledHeight();
        float centerX = windowWidth / 2f;
        float centerY = windowHeight / 2f;

        for (Entity entity : entities) {
            float[] position = projections.get(entity);
            if (position[2] < 0f || position[2] >= 1f) {
                position[0] = display.getScaledWidth() - position[0];
                position[1] = display.getScaledHeight() - position[1];
            }

            GlStateManager.pushMatrix();
            GL11.glEnable(GL11.GL_LINE_SMOOTH);
            GL11.glLineWidth(1f);
            VisualHelper.MC_GFX.drawLine(centerX, centerY, position[0], position[1], EntityHelper.getColor(entity));
            GL11.glDisable(GL11.GL_LINE_SMOOTH);
            GlStateManager.popMatrix();
        }
    }

    private boolean qualifies(Entity entity) {
        return ((this.players.getValue() && EntityHelper.isOtherPlayer(entity) && !AntiBot.isBot((EntityPlayer) entity))
                || (this.hostiles.getValue() && EntityHelper.isHostile(entity))
                || (this.animals.getValue() && EntityHelper.isAnimal(entity))
                || (this.passive.getValue() && EntityHelper.isPassive(entity))
                || (this.items.getValue() && entity instanceof EntityItem))
                && (this.invisibles.getValue() || !entity.isInvisible());
    }
}
