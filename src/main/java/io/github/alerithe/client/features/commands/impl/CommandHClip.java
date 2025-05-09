package io.github.alerithe.client.features.commands.impl;

import io.github.alerithe.client.features.commands.Command;
import io.github.alerithe.client.features.commands.ErrorMessages;
import io.github.alerithe.client.utilities.MathHelper;
import io.github.alerithe.client.utilities.Wrapper;

public class CommandHClip extends Command {
    public CommandHClip() {
        super("hclip", new String[0], "<distance>");
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 1) {
            Wrapper.printMessage(ErrorMessages.NOT_ENOUGH_ARGS);
            return;
        }

        if (!MathHelper.isDouble(args[0])) {
            Wrapper.printMessage(ErrorMessages.INVALID_ARG_TYPE);
            return;
        }

        double distance = Double.parseDouble(args[0]);
        float[] vector = Wrapper.getPlayer().getMoveVector();
        Wrapper.getPlayer().setPosition(Wrapper.getPlayer().posX + (vector[0] * distance), Wrapper.getPlayer().posY,
                Wrapper.getPlayer().posZ + (vector[1] * distance));
        Wrapper.printMessage(String.format("Teleported %f blocks horizontally.", distance));
    }
}
