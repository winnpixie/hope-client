package io.github.alerithe.client.features.modules.impl.visual;

import io.github.alerithe.client.events.EventDraw;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.features.modules.impl.combat.AntiBot;
import io.github.alerithe.client.features.properties.Property;
import io.github.alerithe.client.features.properties.impl.NumberProperty;
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

public class Radar extends Module {
    private final Property<Boolean> players = new Property<>("Players", new String[0], true);
    private final Property<Boolean> monsters = new Property<>("Monsters", new String[]{"mobs"}, false);
    private final Property<Boolean> animals = new Property<>("Animals", new String[0], false);
    private final Property<Boolean> passive = new Property<>("Passive", new String[0], false);
    private final Property<Boolean> invisibles = new Property<>("Invisibles", new String[]{"invis"}, true);
    private final Property<Boolean> items = new Property<>("Items", new String[0], false);
    private final NumberProperty<Integer> x = new NumberProperty<>("X", new String[0], 2, 0,
            Toolkit.getDefaultToolkit().getScreenSize().width);
    private final NumberProperty<Integer> y = new NumberProperty<>("Y", new String[0], 85, 0,
            Toolkit.getDefaultToolkit().getScreenSize().height);
    private final NumberProperty<Integer> size = new NumberProperty<>("Size", new String[0], 50, 25, Integer.MAX_VALUE);

    public Radar() {
        super("Radar", new String[0], Type.VISUAL);

        getPropertyManager().add(players);
        getPropertyManager().add(monsters);
        getPropertyManager().add(animals);
        getPropertyManager().add(passive);
        getPropertyManager().add(invisibles);
        getPropertyManager().add(items);
        getPropertyManager().add(size);
        getPropertyManager().add(x);
        getPropertyManager().add(y);
    }

    @Register(CallOrder.LAST)
    private void onOverlayDraw(EventDraw.Overlay event) {
        if (Wrapper.getGameSettings().showDebugInfo) return;

        int x = this.x.getValue();
        int y = this.y.getValue();
        int width = this.size.getValue();
        int height = this.size.getValue();
        float cx = x + (width / 2f);
        float cy = y + (height / 2f);

        GL11.glPushMatrix();

        VisualHelper.drawBorderedRect(x, y, x + width, y + height, 1, 0xFF222222, 0xFF444444);
        VisualHelper.drawRectSized(x + (width / 2f) - 0.5f, y, 1, height, 0xFF444444);
        VisualHelper.drawRectSized(x, y + (height / 2f) - 0.5f, width, 1, 0xFF444444);
        VisualHelper.drawRectSized(cx - 1, cy - 1, 2, 2, 0xFFFFFF00);

        int maxDist = size.getValue() / 2;
        for (Entity entity : Wrapper.getWorld().loadedEntityList) {
            if (!qualifies(entity)) continue;

            // X difference
            double dx = MathHelper.lerpd(entity.prevPosX, entity.posX, event.getPartialTicks())
                    - MathHelper.lerpd(Wrapper.getPlayer().prevPosX, Wrapper.getPlayer().posX, event.getPartialTicks());
            // Z difference
            double dz = MathHelper.lerpd(entity.prevPosZ, entity.posZ, event.getPartialTicks())
                    - MathHelper.lerpd(Wrapper.getPlayer().prevPosZ, Wrapper.getPlayer().posZ, event.getPartialTicks());

            // Make sure they're within the available rendering range
            if ((dx * dx + dz * dz) <= (maxDist * maxDist)) {
                float dist = net.minecraft.util.MathHelper.sqrt_double(dx * dx + dz * dz);
                float[] vector = Wrapper.getPlayer().getLookVector(
                        Wrapper.getPlayer().getRotationsToEntity(entity)[0]
                                - MathHelper.lerpf(Wrapper.getPlayer().prevRotationYawHead,
                                Wrapper.getPlayer().rotationYawHead, event.getPartialTicks()));
                VisualHelper.drawRectSized(cx - 1 - (vector[0] * dist), cy - 1 - (vector[1] * dist), 2, 2,
                        0xFFFF0000);
            }
        }

        GL11.glPopMatrix();
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
