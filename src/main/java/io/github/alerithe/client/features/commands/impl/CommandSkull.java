package io.github.alerithe.client.features.commands.impl;

import io.github.alerithe.client.features.commands.Command;
import io.github.alerithe.client.features.commands.ErrorMessages;
import io.github.alerithe.client.utilities.EntityHelper;
import io.github.alerithe.client.utilities.GameHelper;
import io.github.alerithe.client.utilities.NetworkHelper;
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
        if (!EntityHelper.getUser().capabilities.isCreativeMode) {
            GameHelper.printChatMessage(ErrorMessages.format("You must be in Creative mode to use this."));
            return;
        }

        if (args.length < 1) {
            GameHelper.printChatMessage(ErrorMessages.NOT_ENOUGH_ARGS);
            return;
        }

        int slot = EntityHelper.getUser().inventory.currentItem + 36; // Set the item to spawn in their selected slot
        ItemStack stack = new ItemStack(Items.skull, 64, 3);
        NBTTagCompound compound = new NBTTagCompound();
        compound.setString("SkullOwner", args[0]);
        stack.setTagCompound(compound);
        NetworkHelper.sendPacket(new C10PacketCreativeInventoryAction(slot, stack));
        GameHelper.printChatMessage(String.format("Gave you the skull of %s", args[0]));
    }
}
