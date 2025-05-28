package io.github.alerithe.client.features.commands.impl;

import io.github.alerithe.client.features.commands.Command;
import io.github.alerithe.client.features.commands.ErrorMessages;
import io.github.alerithe.client.utilities.EntityHelper;
import io.github.alerithe.client.utilities.GameHelper;
import io.github.alerithe.client.utilities.MathHelper;

public class CommandHClip extends Command {
    public CommandHClip() {
        super("hclip", new String[0], "<distance>");
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
        float[] vector = EntityHelper.getUser().getMoveVector();
        EntityHelper.getUser().setPosition(EntityHelper.getUser().posX + (vector[0] * distance), EntityHelper.getUser().posY,
                EntityHelper.getUser().posZ + (vector[1] * distance));
        GameHelper.printChatMessage(String.format("Teleported %f blocks horizontally.", distance));
    }
}
