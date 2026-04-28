package io.github.alerithe.client.features.modules.impl.visual;

import io.github.alerithe.client.events.bus.Subscribe;
import io.github.alerithe.client.events.game.EventDraw;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.features.modules.impl.combat.AntiBot;
import io.github.alerithe.client.features.properties.impl.BooleanProperty;
import io.github.alerithe.client.utilities.EntityHelper;
import io.github.alerithe.client.utilities.GameHelper;
import io.github.alerithe.client.utilities.MathHelper;
import io.github.alerithe.client.utilities.WorldHelper;
import io.github.alerithe.client.utilities.graphics.VisualHelper;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;
import java.util.Map;

public class Tracers extends Module {
    private final BooleanProperty players = new BooleanProperty("Players", new String[0], true);
    private final BooleanProperty hostiles = new BooleanProperty("Hostiles", new String[]{"monsters", "mobs"}, false);
    private final BooleanProperty animals = new BooleanProperty("Animals", new String[0], false);
    private final BooleanProperty passive = new BooleanProperty("Passive", new String[0], false);
    private final BooleanProperty invisibles = new BooleanProperty("Invisibles", new String[]{"invis"}, true);
    private final BooleanProperty items = new BooleanProperty("Items", new String[0], false);

    private final Map<Entity, float[]> projections = new HashMap<>();

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
    public void onWorldDraw(EventDraw.World event) {
        projections.clear();

        for (Entity entity : WorldHelper.getWorld().loadedEntityList) {
            if (!qualifies(entity)) {
                continue;
            }

            double x = (MathHelper.lerpd(entity.prevPosX, entity.posX, event.getPartialTicks())
                    - GameHelper.getGame().getRenderManager().viewerPosX);
            double y = (MathHelper.lerpd(entity.prevPosY, entity.posY, event.getPartialTicks())
                    - GameHelper.getGame().getRenderManager().viewerPosY);
            double z = (MathHelper.lerpd(entity.prevPosZ, entity.posZ, event.getPartialTicks())
                    - GameHelper.getGame().getRenderManager().viewerPosZ);

            float[] projection = VisualHelper.project((float) x, (float) y, (float) z);
            if (projection.length == 0) {
                continue;
            }

            projections.put(entity, projection);
        }
    }

    @Subscribe
    public void onOverlayDraw(EventDraw.Overlay event) {
        ScaledResolution display = VisualHelper.getDisplay();
        float windowWidth = display.getScaledWidth();
        float windowHeight = display.getScaledHeight();
        float centerX = windowWidth / 2f;
        float centerY = windowHeight / 2f;

        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glLineWidth(2f);

        for (Map.Entry<Entity, float[]> projection : projections.entrySet()) {
            Entity entity = projection.getKey();
            float[] position = projection.getValue();

            float x = position[0];
            float y = position[1];
            float z = position[2];

            if (z < 0f || z >= 1f) {
                x = windowWidth - x;
                y = windowHeight - y;
            }

            VisualHelper.MC_GFX.drawLine(centerX, centerY, x, y, EntityHelper.getColor(entity));
        }

        GL11.glDisable(GL11.GL_LINE_SMOOTH);
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
