package io.github.alerithe.client.features.commands.impl;

import io.github.alerithe.client.features.commands.Command;
import io.github.alerithe.client.features.commands.ErrorMessages;
import io.github.alerithe.client.utilities.MathHelper;
import io.github.alerithe.client.utilities.Wrapper;
import net.minecraft.entity.Entity;

public class CommandEntityInfo extends Command {
    public CommandEntityInfo() {
        super("entityinfo", new String[]{"entity", "ei"}, "<id/name|iname> <id|name>");
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            Wrapper.printMessage(ErrorMessages.NOT_ENOUGH_ARGS);
            return;
        }

        Entity target = null;
        switch (args[0].toLowerCase()) {
            case "id":
                if (!MathHelper.isInt(args[1])) {
                    Wrapper.printMessage(ErrorMessages.INVALID_ARG_TYPE);
                    return;
                }

                int entityId = Integer.parseInt(args[1]);
                target = Wrapper.getWorld().getEntityByID(entityId);
                break;
            case "name":
                for (Entity entity : Wrapper.getWorld().loadedEntityList) {
                    if (entity.getName().equals(args[1])) target = entity;
                }
                break;
            case "iname":
                for (Entity entity : Wrapper.getWorld().loadedEntityList) {
                    if (entity.getName().equalsIgnoreCase(args[1])) target = entity;
                }
                break;
            default:
                Wrapper.printMessage(ErrorMessages.INVALID_ARG);
                return;
        }

        if (target == null) {
            Wrapper.printMessage(ErrorMessages.INVALID_TARGET);
            return;
        }

        printEntityInfo(target);
    }

    private void printEntityInfo(Entity entity) {
        Wrapper.printMessage("\247eID: \247r" + entity.getEntityId());
        Wrapper.printMessage("\247eType: \247r" + entity.getClass().getSimpleName());
        Wrapper.printMessage("\247eName: \247r" + entity.getName());
        Wrapper.printMessage("\247eDisplay Name: \247r" + entity.getDisplayName().getFormattedText());
        Wrapper.printMessage("\247ePosition: \247r" + String.format("X %.1f Y %.1f Z %.1f",
                entity.posX, entity.posY, entity.posZ));
        Wrapper.printMessage("\247eRotation: \247r" + String.format("Yaw %.1f, Pitch %.1f",
                entity.rotationYaw, entity.rotationPitch));
    }
}
