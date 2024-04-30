package io.github.alerithe.client.features.modules.impl.miscellaneous;

import io.github.alerithe.client.events.EventUpdate;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.utilities.Wrapper;
import io.github.alerithe.events.Register;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class CreativeDrop extends Module {
    public CreativeDrop() {
        super("CreativeDrop", new String[]{"creativedrop", "dropspammer", "dropspam"}, Type.MISCELLANEOUS);
    }

    @Register
    private void onPreUpdate(EventUpdate.Pre event) {
        if (!Wrapper.getPlayer().capabilities.isCreativeMode) return;

        ItemStack stack = new ItemStack(Items.cake, 64);
        stack.setStackDisplayName("\2474#HopeClient");
        for (Enchantment enchantment : Enchantment.enchantmentsBookList) {
            stack.addEnchantment(enchantment, Byte.MAX_VALUE);
        }

        Wrapper.sendPacket(new C10PacketCreativeInventoryAction(36 + Wrapper.getPlayer().inventory.currentItem, stack));
        Wrapper.sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.DROP_ALL_ITEMS, BlockPos.ORIGIN,
                EnumFacing.DOWN));
    }
}
