package io.github.alerithe.client.features.modules.impl.visual;

import io.github.alerithe.client.events.game.EventDraw;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.features.modules.impl.combat.AntiBot;
import io.github.alerithe.client.features.modules.impl.visual.entityesp.EntityESPMode;
import io.github.alerithe.client.features.modules.impl.visual.entityesp.Pillar;
import io.github.alerithe.client.features.modules.impl.visual.entityesp.Rectangle;
import io.github.alerithe.client.features.properties.impl.BooleanProperty;
import io.github.alerithe.client.features.properties.impl.ObjectProperty;
import io.github.alerithe.client.utilities.graphics.VisualHelper;
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

public class EntityESP extends Module {
    private final ObjectProperty<EntityESPMode> mode = new ObjectProperty<>("Mode", new String[0], new Rectangle(this),
            new Pillar(this));
    private final BooleanProperty players = new BooleanProperty("Players", new String[0], true);
    private final BooleanProperty monsters = new BooleanProperty("Monsters", new String[]{"mobs"}, false);
    private final BooleanProperty animals = new BooleanProperty("Animals", new String[0], false);
    private final BooleanProperty passive = new BooleanProperty("Passive", new String[0], false);
    private final BooleanProperty invisibles = new BooleanProperty("Invisibles", new String[]{"invis"}, true);
    private final BooleanProperty items = new BooleanProperty("Items", new String[0], false);
    public final BooleanProperty showHealth = new BooleanProperty("ShowHealth", new String[]{"healthbar", "health"}, true);
    public final BooleanProperty showNames = new BooleanProperty("ShowNames", new String[]{"names"}, true);

    public EntityESP() {
        super("ESP", new String[0], Type.VISUAL);

        getPropertyManager().add(mode);
        getPropertyManager().add(players);
        getPropertyManager().add(monsters);
        getPropertyManager().add(animals);
        getPropertyManager().add(passive);
        getPropertyManager().add(invisibles);
        getPropertyManager().add(items);
        getPropertyManager().add(showHealth);
        getPropertyManager().add(showNames);
    }

    @Register(CallOrder.IMPORTANT)
    private void onOverlayDraw(EventDraw.Overlay event) {
        mode.getValue().onOverlayDraw(event);
    }

    @Register(CallOrder.IMPORTANT)
    private void onWorldDraw(EventDraw.World event) {
        mode.getValue().onWorldDraw(event);
    }

    @Register
    private void onTagDraw(EventDraw.Tag event) {
        event.setCancelled(showNames.getValue() && qualifies(event.getEntity()));
    }

    public boolean qualifies(Entity entity) {
        return ((entity instanceof EntityPlayer && this.players.getValue() && !AntiBot.isBot((EntityPlayer) entity))
                || (entity instanceof EntityMob && this.monsters.getValue())
                || ((entity instanceof EntityAnimal || entity instanceof EntitySquid) && this.animals.getValue())
                || ((entity instanceof EntityVillager || entity instanceof EntityGolem) && this.passive.getValue())
                || (entity instanceof EntityItem && items.getValue()))
                && (!entity.isInvisible() || this.invisibles.getValue()) && VisualHelper.isInView(entity)
                && entity != Wrapper.getPlayer();
    }
}
