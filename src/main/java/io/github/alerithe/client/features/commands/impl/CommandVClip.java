package io.github.alerithe.client.features.commands.impl;

import io.github.alerithe.client.features.commands.Command;
import io.github.alerithe.client.features.commands.ErrorMessages;
import io.github.alerithe.client.utilities.EntityHelper;
import io.github.alerithe.client.utilities.GameHelper;
import io.github.alerithe.client.utilities.MathHelper;

public class CommandVClip extends Command {
    public CommandVClip() {
        super("vclip", new String[0], "<distance>");
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 1) {
            GameHelper.printChatMessage(ErrorMessages.NOT_ENOUGH_ARGS);
            return;
        }

        if (!MathHelper.isDouble(args[0])) {
            GameHelper.printChatMessage(ErrorMessages.INVALID_ARG_TYPE);
            return;
        }

        double distance = Double.parseDouble(args[0]);
        EntityHelper.getUser().setPosition(EntityHelper.getUser().posX, EntityHelper.getUser().posY + distance,
                EntityHelper.getUser().posZ);
        GameHelper.printChatMessage(String.format("Teleported %f blocks vertically.", distance));
    }
}
