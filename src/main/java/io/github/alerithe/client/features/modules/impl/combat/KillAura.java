package io.github.alerithe.client.features.modules.impl.combat;

import io.github.alerithe.client.Client;
import io.github.alerithe.client.events.game.EventUpdate;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.features.modules.impl.combat.aura.*;
import io.github.alerithe.client.features.modules.impl.movement.NoSlowdown;
import io.github.alerithe.client.features.properties.impl.BooleanProperty;
import io.github.alerithe.client.features.properties.impl.DoubleProperty;
import io.github.alerithe.client.features.properties.impl.IntProperty;
import io.github.alerithe.client.features.properties.impl.ObjectProperty;
import io.github.alerithe.client.utilities.*;
import io.github.alerithe.events.impl.Subscribe;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class KillAura extends Module {
    private final ObjectProperty<AuraMode> mode = new ObjectProperty<>("Mode", new String[0], new Switch(this),
            new Tick(this), new Single(this), new LockOn(this));
    private final ObjectProperty<SortingMode> sortingMode = new ObjectProperty<>("SortingMode", new String[]{"sorting"},
            new SortingMode.AngleSort(), new SortingMode.DistanceSort());
    public final IntProperty minAps = new IntProperty("MinHitsPerSecond", new String[]{"minaps", "mincps", "minspeed"},
            8, 1, 20);
    public final IntProperty maxAps = new IntProperty("MaxHitsPerSecond", new String[]{"maxaps", "maxcps", "maxspeed"},
            12, 1, 20);
    private final DoubleProperty angleOffset = new DoubleProperty("AngleOffset", new String[0],
            2.5, 0.0, 5.0);
    public final DoubleProperty distance = new DoubleProperty("Distance", new String[]{"reach", "range", "dist", "distance"},
            4.0, 0.1, 6.0);
    private final BooleanProperty smartAngles = new BooleanProperty("SmartAngles", new String[0], false);
    private final BooleanProperty look = new BooleanProperty("Look", new String[0], false);
    public final BooleanProperty autoBlock = new BooleanProperty("AutoBlock", new String[]{"ab"}, true);
    private final BooleanProperty players = new BooleanProperty("Players", new String[0], true);
    private final BooleanProperty hostiles = new BooleanProperty("Hostiles", new String[]{"monsters", "mobs"}, false);
    private final BooleanProperty animals = new BooleanProperty("Animals", new String[0], false);
    private final BooleanProperty passive = new BooleanProperty("Passive", new String[0], false);
    private final BooleanProperty invisibles = new BooleanProperty("Invisibles", new String[]{"invis"}, true);
    private final BooleanProperty attackFriends = new BooleanProperty("AttackFriends", new String[]{"hitfriends"}, false);

    public List<EntityLivingBase> near;
    public EntityLivingBase target;
    public boolean attacking;

    public KillAura() {
        super("KillAura", new String[]{"aura", "ka"}, Type.COMBAT);

        getPropertyManager().add(mode);
        getPropertyManager().add(minAps);
        getPropertyManager().add(maxAps);
        getPropertyManager().add(distance);
        getPropertyManager().add(autoBlock);
        getPropertyManager().add(sortingMode);
        getPropertyManager().add(smartAngles);
        getPropertyManager().add(angleOffset);
        getPropertyManager().add(look);
        getPropertyManager().add(players);
        getPropertyManager().add(hostiles);
        getPropertyManager().add(animals);
        getPropertyManager().add(passive);
        getPropertyManager().add(invisibles);
        getPropertyManager().add(attackFriends);
    }

    @Subscribe
    private void onPreUpdate(EventUpdate.Pre event) {
        near = getNear();

        mode.getValue().onPreUpdate(event);
        if (target == null) return;

        float[] angles = getRotationsToTarget(target);
        float offsetValue = angleOffset.getValue().floatValue();
        float yawDelta = (((angles[0] - (event.getYaw() % 360)) + 180f) % 360f) - 180f;
        event.setYaw(event.getYaw() + yawDelta + MathHelper.getRandomFloat(-offsetValue, offsetValue));
        event.setPitch(MathHelper.clamp(angles[1] + MathHelper.getRandomFloat(-offsetValue, offsetValue), -90f, 90f));

        if (look.getValue()) {
            EntityHelper.getUser().rotationYaw = event.getYaw();
            EntityHelper.getUser().rotationPitch = event.getPitch();
        }

        if (EntityHelper.getUser().getHeldItem() != null
                && EntityHelper.getUser().getHeldItem().getItem() instanceof ItemSword
                && (autoBlock.getValue() || EntityHelper.getUser().isBlocking())) {
            if (!attacking) {
                GameHelper.getController().sendUseItem(EntityHelper.getUser(), WorldHelper.getWorld(),
                        EntityHelper.getUser().getHeldItem());
            }

            EntityHelper.getUser().setItemInUse(EntityHelper.getUser().getHeldItem(),
                    EntityHelper.getUser().getHeldItem().getMaxItemUseDuration());

            if (!GameHelper.getSettings().keyBindUseItem.isKeyDown()
                    && !Client.MODULE_MANAGER.find(NoSlowdown.class).isEnabled()) {
                EntityHelper.getUser().setSpeed(EntityHelper.getUser().getSpeed() * 0.2);
                EntityHelper.getUser().setSprinting(false);
            }
        }
    }

    @Subscribe
    private void onPostUpdate(EventUpdate.Post event) {
        if (!attacking) return;

        attacking = false;
        if (EntityHelper.getUser().isBlocking()) {
            NetworkHelper.sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM,
                    BlockPos.ORIGIN, EnumFacing.DOWN));
        }

        attack(target);
    }

    public List<EntityLivingBase> getNear() {
        List<EntityLivingBase> nearby = new ArrayList<>();
        for (Entity entity : WorldHelper.getWorld().loadedEntityList) {
            if (qualifies(entity)) nearby.add((EntityLivingBase) entity);
        }

        nearby.sort(sortingMode.getValue().comparator);
        return nearby;
    }

    public boolean qualifies(Entity entity) {
        return ((this.players.getValue() && EntityHelper.isOtherPlayer(entity) && !AntiBot.isBot((EntityPlayer) entity))
                || (this.hostiles.getValue() && EntityHelper.isHostile(entity))
                || (this.animals.getValue() && EntityHelper.isAnimal(entity))
                || (this.passive.getValue() && EntityHelper.isPassive(entity))
                && (this.invisibles.getValue() || !entity.isInvisible())
                && EntityHelper.hasHeartBeat(entity))
                && WorldHelper.distanceSq(entity) <= (distance.getValue() * distance.getValue())
                && (attackFriends.getValue() || Client.FRIEND_MANAGER.find(entity.getName()) == null);
    }

    private float[] getRotationsToTarget(Entity target) {
        if (!smartAngles.getValue()) return EntityHelper.getRotationToEntity(target);

        double yDiff = target.posY - EntityHelper.getUser().posY;
        if (yDiff > 0.4) {
            return EntityHelper.getRotationToPosition(
                    target.posX,
                    target.posY + (target.getEyeHeight() / (yDiff / 0.4)),
                    target.posZ);
        }

        return EntityHelper.getRotationToEntity(target);
    }

    public void attack(Entity entity) {
        EntityHelper.getUser().swingItem();
        NetworkHelper.sendPacket(new C02PacketUseEntity(entity, C02PacketUseEntity.Action.ATTACK));
    }

    private static class SortingMode extends ObjectProperty.Value {
        private final Comparator<Entity> comparator;

        public SortingMode(String name, String[] aliases, Comparator<Entity> comparator) {
            super(name, aliases);
            this.comparator = comparator;
        }

        private static class AngleSort extends SortingMode {
            public AngleSort() {
                super("Angle", new String[0], Comparator.comparingDouble(entity ->
                        EntityHelper.getRotationToEntity(entity)[0]));
            }
        }

        private static class DistanceSort extends SortingMode {
            public DistanceSort() {
                super("Distance", new String[]{"dist"}, Comparator.comparingDouble(WorldHelper::distanceSq));
            }
        }
    }
}
