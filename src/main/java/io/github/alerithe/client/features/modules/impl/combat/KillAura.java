package io.github.alerithe.client.features.modules.impl.combat;

import io.github.alerithe.client.Client;
import io.github.alerithe.client.events.EventUpdate;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.features.modules.impl.combat.aura.*;
import io.github.alerithe.client.features.modules.impl.movement.NoSlowdown;
import io.github.alerithe.client.features.properties.impl.BooleanProperty;
import io.github.alerithe.client.features.properties.impl.DoubleProperty;
import io.github.alerithe.client.features.properties.impl.IntProperty;
import io.github.alerithe.client.features.properties.impl.ObjectProperty;
import io.github.alerithe.client.utilities.MathHelper;
import io.github.alerithe.client.utilities.Wrapper;
import io.github.alerithe.events.Register;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
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
    private final BooleanProperty look = new BooleanProperty("Look", new String[0], false);
    public final IntProperty minAps = new IntProperty("MinHitsPerSecond", new String[]{"minaps", "mincps", "minspeed"},
            8, 1, 20);
    public final IntProperty maxAps = new IntProperty("MaxHitsPerSecond", new String[]{"maxaps", "maxcps", "maxspeed"},
            12, 1, 20);
    public final DoubleProperty distance = new DoubleProperty("Distance", new String[]{"reach", "range", "dist", "distance"},
            4, 0.1, 6);
    public final BooleanProperty autoBlock = new BooleanProperty("AutoBlock", new String[]{"ab"}, true);
    private final BooleanProperty players = new BooleanProperty("Players", new String[0], true);
    private final BooleanProperty monsters = new BooleanProperty("Monsters", new String[]{"mobs"}, false);
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
        getPropertyManager().add(look);
        getPropertyManager().add(players);
        getPropertyManager().add(monsters);
        getPropertyManager().add(animals);
        getPropertyManager().add(passive);
        getPropertyManager().add(invisibles);
        getPropertyManager().add(attackFriends);
    }

    @Register
    private void onPreUpdate(EventUpdate.Pre event) {
        near = getNear();

        mode.getValue().onPreUpdate(event);
        if (target == null) return;

        float[] angles = Wrapper.getPlayer().getRotationsToEntity(target);
        event.setYaw(MathHelper.clamp(angles[0] + MathHelper.getRandomFloat(-4f, 4f), -360f, 360f));
        event.setPitch(MathHelper.clamp(angles[1] + MathHelper.getRandomFloat(-4f, 4f), -90f, 90f));

        if (look.getValue()) {
            Wrapper.getPlayer().rotationYaw = event.getYaw();
            Wrapper.getPlayer().rotationPitch = event.getPitch();
        }

        if (Wrapper.getPlayer().getHeldItem() != null
                && Wrapper.getPlayer().getHeldItem().getItem() instanceof ItemSword
                && (autoBlock.getValue() || Wrapper.getPlayer().isBlocking())) {
            if (!attacking) {
                Wrapper.getPlayerController().sendUseItem(Wrapper.getPlayer(), Wrapper.getWorld(),
                        Wrapper.getPlayer().getHeldItem());
            }

            Wrapper.getPlayer().setItemInUse(Wrapper.getPlayer().getHeldItem(),
                    Wrapper.getPlayer().getHeldItem().getMaxItemUseDuration());

            if (!Wrapper.getSettings().keyBindUseItem.isKeyDown()
                    && !Client.MODULE_MANAGER.find(NoSlowdown.class).isEnabled()) {
                Wrapper.getPlayer().setSpeed(Wrapper.getPlayer().getSpeed() * 0.2);
                Wrapper.getPlayer().setSprinting(false);
            }
        }
    }

    @Register
    private void onPostUpdate(EventUpdate.Post event) {
        mode.getValue().onPostUpdate(event);
        if (target == null) return;
        if (!attacking) return;

        attacking = false;
        if (Wrapper.getPlayer().isBlocking()) {
            Wrapper.sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM,
                    BlockPos.ORIGIN, EnumFacing.DOWN));
        }
        attack(target);
    }

    public List<EntityLivingBase> getNear() {
        List<EntityLivingBase> nearby = new ArrayList<>();
        for (Entity entity : Wrapper.getWorld().loadedEntityList) {
            if (qualifies(entity)) nearby.add((EntityLivingBase) entity);
        }

        nearby.sort(sortingMode.getValue().comparator);
        return nearby;
    }

    public boolean qualifies(Entity entity) {
        return ((entity instanceof EntityPlayer && this.players.getValue() && !AntiBot.isBot((EntityPlayer) entity))
                || (entity instanceof EntityMob && this.monsters.getValue())
                || ((entity instanceof EntityAnimal || entity instanceof EntitySquid) && this.animals.getValue())
                || ((entity instanceof EntityVillager || entity instanceof EntityGolem) && this.passive.getValue()))
                && (!entity.isInvisible() || this.invisibles.getValue()) && entity.isEntityAlive()
                && Wrapper.getPlayer().getDistanceSqToEntity(entity) <= (distance.getValue() * distance.getValue())
                && (attackFriends.getValue() || Client.FRIEND_MANAGER.find(entity.getName()) == null)
                && entity != Wrapper.getPlayer();
    }

    public void attack(Entity entity) {
        Wrapper.getPlayer().swingItem();
        Wrapper.sendPacket(new C02PacketUseEntity(entity, C02PacketUseEntity.Action.ATTACK));
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
                        Wrapper.getPlayer().getRotationsToEntity(entity)[0]));
            }
        }

        private static class DistanceSort extends SortingMode {
            public DistanceSort() {
                super("Distance", new String[]{"dist"}, Comparator.comparingDouble(entity ->
                        Wrapper.getPlayer().getDistanceSqToEntity(entity)));
            }
        }
    }
}
