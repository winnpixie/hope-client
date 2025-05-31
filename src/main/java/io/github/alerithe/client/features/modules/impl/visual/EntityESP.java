package io.github.alerithe.client.features.modules.impl.visual;

import io.github.alerithe.client.events.game.EventDraw;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.features.modules.impl.combat.AntiBot;
import io.github.alerithe.client.features.modules.impl.visual.entityesp.EntityESPMode;
import io.github.alerithe.client.features.modules.impl.visual.entityesp.Pillar;
import io.github.alerithe.client.features.modules.impl.visual.entityesp.Rectangle;
import io.github.alerithe.client.features.properties.impl.BooleanProperty;
import io.github.alerithe.client.features.properties.impl.ObjectProperty;
import io.github.alerithe.client.utilities.EntityHelper;
import io.github.alerithe.client.utilities.graphics.VisualHelper;
import io.github.alerithe.events.impl.Subscribe;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;

public class EntityESP extends Module {
    private final ObjectProperty<EntityESPMode> mode = new ObjectProperty<>("Mode", new String[0], new Rectangle(this),
            new Pillar(this));
    private final BooleanProperty players = new BooleanProperty("Players", new String[0], true);
    private final BooleanProperty hostiles = new BooleanProperty("Hostiles", new String[]{"monsters", "mobs"}, false);
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
        getPropertyManager().add(hostiles);
        getPropertyManager().add(animals);
        getPropertyManager().add(passive);
        getPropertyManager().add(invisibles);
        getPropertyManager().add(items);
        getPropertyManager().add(showHealth);
        getPropertyManager().add(showNames);
    }

    @Subscribe
    private void onOverlayDraw(EventDraw.Overlay event) {
        mode.getValue().onOverlayDraw(event);
    }

    @Subscribe
    private void onWorldDraw(EventDraw.World event) {
        mode.getValue().onWorldDraw(event);
    }

    @Subscribe
    private void onTagDraw(EventDraw.Tag event) {
        if (showNames.getValue() && qualifies(event.getEntity())) event.cancel();
    }

    public boolean qualifies(Entity entity) {
        return ((this.players.getValue() && EntityHelper.isOtherPlayer(entity) && !AntiBot.isBot((EntityPlayer) entity))
                || (this.hostiles.getValue() && EntityHelper.isHostile(entity))
                || (this.animals.getValue() && EntityHelper.isAnimal(entity))
                || (this.passive.getValue() && EntityHelper.isPassive(entity))
                || (this.items.getValue() && entity instanceof EntityItem))
                && (this.invisibles.getValue() || !entity.isInvisible())
                && VisualHelper.isInView(entity);
    }
}
