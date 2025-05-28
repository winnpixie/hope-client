package io.github.alerithe.client.features.modules.impl.visual;

import io.github.alerithe.client.events.game.EventDraw;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.features.modules.impl.combat.AntiBot;
import io.github.alerithe.client.features.properties.impl.BooleanProperty;
import io.github.alerithe.client.features.properties.impl.IntProperty;
import io.github.alerithe.client.utilities.EntityHelper;
import io.github.alerithe.client.utilities.GameHelper;
import io.github.alerithe.client.utilities.MathHelper;
import io.github.alerithe.client.utilities.WorldHelper;
import io.github.alerithe.client.utilities.graphics.VisualHelper;
import io.github.alerithe.events.impl.Subscribe;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;

public class Radar extends Module {
    private final BooleanProperty players = new BooleanProperty("Players", new String[0], true);
    private final BooleanProperty hostiles = new BooleanProperty("Hostiles", new String[]{"monsters", "mobs"}, false);
    private final BooleanProperty animals = new BooleanProperty("Animals", new String[0], false);
    private final BooleanProperty passive = new BooleanProperty("Passive", new String[0], false);
    private final BooleanProperty invisibles = new BooleanProperty("Invisibles", new String[]{"invis"}, true);
    private final BooleanProperty items = new BooleanProperty("Items", new String[0], false);
    private final IntProperty x = new IntProperty("X", new String[0],
            2, 0, 8192);
    private final IntProperty y = new IntProperty("Y", new String[0],
            85, 0, 8192);
    private final IntProperty size = new IntProperty("Size", new String[0],
            70, 25, 512);

    public Radar() {
        super("Radar", new String[0], Type.VISUAL);

        getPropertyManager().add(players);
        getPropertyManager().add(hostiles);
        getPropertyManager().add(animals);
        getPropertyManager().add(passive);
        getPropertyManager().add(invisibles);
        getPropertyManager().add(items);
        getPropertyManager().add(size);
        getPropertyManager().add(x);
        getPropertyManager().add(y);
    }

    @Subscribe
    private void onOverlayDraw(EventDraw.Overlay event) {
        if (GameHelper.getSettings().showDebugInfo) return;

        int x = this.x.getValue();
        int y = this.y.getValue();
        int width = this.size.getValue();
        int height = this.size.getValue();
        float centerX = x + (width / 2f);
        float centerY = y + (height / 2f);

        VisualHelper.MC_GFX.drawBorderedSquare(x, y, width, height, 1f, 0xFF111111, 0xFF333333); // Background
        VisualHelper.MC_GFX.drawSquare(x, centerY, width, 1f, 0xFF333333); // Horizontal Bar
        VisualHelper.MC_GFX.drawSquare(centerX, y, 1f, height, 0xFF333333); // Vertical Bar
        VisualHelper.MC_GFX.drawSquare(centerX - 1, centerY - 1, 2, 2, 0xFFFFFF00); // Player

        float maxDist = size.getValue() / 2f;
        for (Entity entity : WorldHelper.getWorld().loadedEntityList) {
            if (!qualifies(entity)) continue;

            // X difference
            double deltaX = MathHelper.lerpd(entity.prevPosX, entity.posX, event.getPartialTicks())
                    - MathHelper.lerpd(EntityHelper.getUser().prevPosX, EntityHelper.getUser().posX, event.getPartialTicks());
            // Z difference
            double deltaZ = MathHelper.lerpd(entity.prevPosZ, entity.posZ, event.getPartialTicks())
                    - MathHelper.lerpd(EntityHelper.getUser().prevPosZ, EntityHelper.getUser().posZ, event.getPartialTicks());

            // Make sure they're within the available rendering range
            if ((deltaX * deltaX + deltaZ * deltaZ) <= (maxDist * maxDist)) {
                float dist = net.minecraft.util.MathHelper.sqrt_double(deltaX * deltaX + deltaZ * deltaZ);
                float[] vector = EntityHelper.getUser().getLookVector(
                        EntityHelper.getRotationToEntity(entity)[0]
                                - MathHelper.lerpf(EntityHelper.getUser().prevRotationYawHead,
                                EntityHelper.getUser().rotationYawHead, event.getPartialTicks()));
                VisualHelper.MC_GFX.drawSquare(centerX - 1 - (vector[0] * dist), centerY - 1 - (vector[1] * dist), 2, 2,
                        EntityHelper.getColor(entity));
            }
        }
    }

    private boolean qualifies(Entity entity) {
        return ((this.players.getValue() && EntityHelper.isOtherPlayer(entity) && !AntiBot.isBot((EntityPlayer) entity))
                || (this.hostiles.getValue() && EntityHelper.isHostile(entity))
                || (this.animals.getValue() && EntityHelper.isAnimal(entity))
                || (this.passive.getValue() && EntityHelper.isPassive(entity))
                || (this.items.getValue() && entity instanceof EntityItem))
                && (this.invisibles.getValue() || !entity.isInvisible())
                && VisualHelper.isInView(entity);
    }
}
