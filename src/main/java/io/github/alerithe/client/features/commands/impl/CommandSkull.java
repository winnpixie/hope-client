package io.github.alerithe.client.features.commands.impl;

import io.github.alerithe.client.features.commands.Command;
import io.github.alerithe.client.features.commands.ErrorMessages;
import io.github.alerithe.client.utilities.Wrapper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;

public class CommandSkull extends Command {
    public CommandSkull() {
        super("skull", new String[0], "<username>");
    }

    @Override
    public void execute(String[] args) {
        if (!Wrapper.getPlayer().capabilities.isCreativeMode) {
            Wrapper.printMessage(ErrorMessages.format("You must be in Creative mode to use this."));
            return;
        }

        if (args.length < 1) {
            Wrapper.printMessage(ErrorMessages.NOT_ENOUGH_ARGS);
            return;
        }

        int slot = Wrapper.getPlayer().inventory.currentItem + 36; // Set the item to spawn in their selected slot
        ItemStack stack = new ItemStack(Items.skull, 64, 3);
        NBTTagCompound compound = new NBTTagCompound();
        compound.setString("SkullOwner", args[0]);
        stack.setTagCompound(compound);
        Wrapper.sendPacket(new C10PacketCreativeInventoryAction(slot, stack));
        Wrapper.printMessage(String.format("Gave you the skull of %s", args[0]));
    }
}
