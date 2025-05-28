package io.github.alerithe.client.features.modules.impl.player;

import io.github.alerithe.client.events.game.EventUpdate;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.features.properties.impl.IntProperty;
import io.github.alerithe.client.utilities.EntityHelper;
import io.github.alerithe.client.utilities.NetworkHelper;
import io.github.alerithe.events.impl.Subscribe;
import net.minecraft.item.*;
import net.minecraft.network.play.client.C03PacketPlayer;

public class FastUse extends Module {
    private final IntProperty useTicks = new IntProperty("UseTicks", new String[]{"ticks"},
            15, 0, 32);

    public FastUse() {
        super("FastUse", new String[]{"fasteat"}, Type.PLAYER);

        getPropertyManager().add(useTicks);
    }

    @Subscribe
    private void onPreUpdate(EventUpdate.Pre event) {
        if (EntityHelper.getUser().isUsingItem() && isUsable(EntityHelper.getUser().getItemInUse())
                && EntityHelper.getUser().onGround && EntityHelper.getUser().getItemInUseDuration() >= useTicks.getValue()) {
            for (int i = 0; i < (32 - useTicks.getValue()); i++) {
                NetworkHelper.sendPacket(new C03PacketPlayer(true));
            }
        }
    }

    private boolean isUsable(ItemStack stack) {
        Item item = stack.getItem();
        return item instanceof ItemFood || item instanceof ItemBucketMilk
                || (item instanceof ItemPotion && ItemPotion.isSplash(stack.getMetadata()));
    }
}
