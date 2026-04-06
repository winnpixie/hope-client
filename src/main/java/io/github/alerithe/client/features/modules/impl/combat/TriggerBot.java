package io.github.alerithe.client.features.modules.impl.combat;

import io.github.alerithe.client.Client;
import io.github.alerithe.client.events.bus.Subscribe;
import io.github.alerithe.client.events.game.EventUpdate;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.features.properties.impl.BooleanProperty;
import io.github.alerithe.client.features.properties.impl.IntProperty;
import io.github.alerithe.client.utilities.EntityHelper;
import io.github.alerithe.client.utilities.GameHelper;
import io.github.alerithe.client.utilities.MathHelper;
import io.github.alerithe.client.utilities.Stopwatch;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class TriggerBot extends Module {
    public final IntProperty minAps = new IntProperty("MinHitsPerSecond", new String[]{"minaps", "mincps", "minspeed"},
            8, 1, 20);
    public final IntProperty maxAps = new IntProperty("MaxHitsPerSecond", new String[]{"maxaps", "maxcps", "maxspeed"},
            12, 1, 20);
    private final BooleanProperty players = new BooleanProperty("Players", new String[0], true);
    private final BooleanProperty hostiles = new BooleanProperty("Hostiles", new String[]{"monsters", "mobs"}, false);
    private final BooleanProperty animals = new BooleanProperty("Animals", new String[0], false);
    private final BooleanProperty passive = new BooleanProperty("Passive", new String[0], false);
    private final BooleanProperty invisibles = new BooleanProperty("Invisibles", new String[]{"invis"}, true);
    private final BooleanProperty attackFriends = new BooleanProperty("AttackFriends", new String[]{"hitfriends"}, false);

    private final Stopwatch timer = new Stopwatch();

    public TriggerBot() {
        super("TriggerBot", new String[0], Type.COMBAT);

        getPropertyManager().add(minAps);
        getPropertyManager().add(maxAps);
        getPropertyManager().add(players);
        getPropertyManager().add(hostiles);
        getPropertyManager().add(animals);
        getPropertyManager().add(passive);
        getPropertyManager().add(invisibles);
        getPropertyManager().add(attackFriends);
    }

    @Subscribe
    private void onPostUpdate(EventUpdate.Post event) {
        if (GameHelper.getGame().objectMouseOver == null) return;
        if (GameHelper.getGame().objectMouseOver.entityHit == null) return;

        Entity entity = GameHelper.getGame().objectMouseOver.entityHit;
        if (!qualifies(entity)) return;

        if (!timer.hasPassed(1000 / MathHelper.getRandomInt(minAps.getValue(), maxAps.getValue()))) return;

        timer.reset();
        GameHelper.getGame().clickMouse();
    }

    public boolean qualifies(Entity entity) {
        return ((this.players.getValue() && EntityHelper.isOtherPlayer(entity) && !AntiBot.isBot((EntityPlayer) entity))
                || (this.hostiles.getValue() && EntityHelper.isHostile(entity))
                || (this.animals.getValue() && EntityHelper.isAnimal(entity))
                || (this.passive.getValue() && EntityHelper.isPassive(entity))
                && (this.invisibles.getValue() || !entity.isInvisible())
                && EntityHelper.hasHeartBeat(entity))
                && (attackFriends.getValue() || Client.FRIEND_MANAGER.find(entity.getName()) == null);
    }
}
