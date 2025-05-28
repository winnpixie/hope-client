package io.github.alerithe.client.features.commands.impl;

import io.github.alerithe.client.features.commands.Command;
import io.github.alerithe.client.features.commands.ErrorMessages;
import io.github.alerithe.client.utilities.GameHelper;
import io.github.alerithe.client.utilities.MathHelper;
import io.github.alerithe.client.utilities.WorldHelper;
import net.minecraft.entity.Entity;

public class CommandEntityInfo extends Command {
    public CommandEntityInfo() {
        super("entityinfo", new String[]{"entity", "ei"}, "<id/name|iname> <id|name>");
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            GameHelper.printChatMessage(ErrorMessages.NOT_ENOUGH_ARGS);
            return;
        }

        Entity target = null;
        switch (args[0].toLowerCase()) {
            case "id":
                if (!MathHelper.isInt(args[1])) {
                    GameHelper.printChatMessage(ErrorMessages.INVALID_ARG_TYPE);
                    return;
                }

                int entityId = Integer.parseInt(args[1]);
                target = WorldHelper.getWorld().getEntityByID(entityId);
                break;
            case "name":
                for (Entity entity : WorldHelper.getWorld().loadedEntityList) {
                    if (entity.getName().equals(args[1])) target = entity;
                }
                break;
            case "iname":
                for (Entity entity : WorldHelper.getWorld().loadedEntityList) {
                    if (entity.getName().equalsIgnoreCase(args[1])) target = entity;
                }
                break;
            default:
                GameHelper.printChatMessage(ErrorMessages.INVALID_ARG);
                return;
        }

        if (target == null) {
            GameHelper.printChatMessage(ErrorMessages.INVALID_TARGET);
            return;
        }

        printEntityInfo(target);
    }

    private void printEntityInfo(Entity entity) {
        GameHelper.printChatMessage("\247eID: \247r" + entity.getEntityId());
        GameHelper.printChatMessage("\247eType: \247r" + entity.getClass().getSimpleName());
        GameHelper.printChatMessage("\247eName: \247r" + entity.getName());
        GameHelper.printChatMessage("\247eDisplay Name: \247r" + entity.getDisplayName().getFormattedText());
        GameHelper.printChatMessage("\247ePosition: \247r" + String.format("X %.1f Y %.1f Z %.1f",
                entity.posX, entity.posY, entity.posZ));
        GameHelper.printChatMessage("\247eRotation: \247r" + String.format("Yaw %.1f, Pitch %.1f",
                entity.rotationYaw, entity.rotationPitch));
    }
}
