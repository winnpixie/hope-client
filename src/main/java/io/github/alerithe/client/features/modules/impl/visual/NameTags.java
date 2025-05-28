package io.github.alerithe.client.features.modules.impl.visual;

import io.github.alerithe.client.Client;
import io.github.alerithe.client.events.game.EventDraw;
import io.github.alerithe.client.features.friends.Friend;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.features.modules.impl.combat.AntiBot;
import io.github.alerithe.client.features.properties.impl.BooleanProperty;
import io.github.alerithe.client.utilities.*;
import io.github.alerithe.client.utilities.graphics.VisualHelper;
import io.github.alerithe.events.impl.Subscribe;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;

import java.util.*;

public class NameTags extends Module {
    private final BooleanProperty players = new BooleanProperty("Players", new String[0], true);
    private final BooleanProperty hostiles = new BooleanProperty("Hostiles", new String[]{"monsters", "mobs"}, false);
    private final BooleanProperty animals = new BooleanProperty("Animals", new String[0], false);
    private final BooleanProperty passive = new BooleanProperty("Passive", new String[0], false);
    private final BooleanProperty invisibles = new BooleanProperty("Invisibles", new String[]{"invis"}, true);
    private final BooleanProperty items = new BooleanProperty("Items", new String[0], false);
    private final BooleanProperty ping = new BooleanProperty("ShowPing", new String[0], true);
    private final BooleanProperty showHealth = new BooleanProperty("ShowHealth", new String[]{"hp"}, true);

    public NameTags() {
        super("NameTags", new String[]{"tags"}, Type.VISUAL);

        getPropertyManager().add(players);
        getPropertyManager().add(hostiles);
        getPropertyManager().add(animals);
        getPropertyManager().add(passive);
        getPropertyManager().add(invisibles);
        getPropertyManager().add(items);
        getPropertyManager().add(ping);
        getPropertyManager().add(showHealth);
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
                    + entity.height + 0.2 - GameHelper.getGame().getRenderManager().viewerPosY);
            double z = (MathHelper.lerpd(entity.prevPosZ, entity.posZ, event.getPartialTicks())
                    - GameHelper.getGame().getRenderManager().viewerPosZ);

            float[] projection = VisualHelper.project((float) x, (float) y, (float) z);

            if (projection == null) continue;
            if (projection[2] < 0f) continue;
            if (projection[2] >= 1f) continue;

            entities.add(entity);
            projections.put(entity, projection);
        }
        GameHelper.getGame().entityRenderer.setupOverlayRendering();
        GlStateManager.popMatrix();
        entities.sort(Comparator.comparingDouble(entity -> -WorldHelper.distanceSq(entity)));

        for (Entity entity : entities) {
            String text = entity.getDisplayName().getFormattedText();
            if (entity instanceof EntityItem) {
                text = ((EntityItem) entity).getEntityItem().getDisplayName();
            } else {
                Friend friend = Client.FRIEND_MANAGER.find(entity.getName());
                if (friend != null) {
                    text = String.format("\247b%s", friend.getAliases()[0]);
                }
            }

            if (ping.getValue() && entity instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) entity;
                if (player.getGameProfile() != null) {
                    NetworkPlayerInfo npi = NetworkHelper.getInfo(player);
                    if (npi != null) {
                        text = String.format("\247a%dms\247r %s", npi.getResponseTime(), text);
                    }
                }
            }

            if (showHealth.getValue() && entity instanceof EntityLivingBase) {
                EntityLivingBase living = (EntityLivingBase) entity;
                text += String.format(" \247%s%d\u2764",
                        EntityHelper.getHealthColorCode(living), MathHelper.ceil(EntityHelper.getTotalHealth(living)));
            }

            float[] position = projections.get(entity);
            GlStateManager.pushMatrix();
            GlStateManager.translate(position[0], position[1], 0f);
            GlStateManager.scale(0.5f, 0.5f, 1f);
            float width = VisualHelper.MC_FONT.getStringWidth(text);

            VisualHelper.MC_GFX.drawBorderedSquare(-width / 2f - 1f, -1f, width + 2f, 10f, 1f, 0x69000000, 0x69AAAAAA);
            VisualHelper.MC_FONT.drawStringWithShadow(text, -width / 2f, 0, -1);
            GlStateManager.popMatrix();
        }
    }

    @Subscribe
    private void onTagDraw(EventDraw.Tag event) {
        if (qualifies(event.getEntity())) event.setCancelled(true);
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
