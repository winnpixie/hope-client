package io.github.alerithe.client.features.commands.impl;

import io.github.alerithe.client.features.commands.Command;
import io.github.alerithe.client.utilities.MathHelper;
import io.github.alerithe.client.utilities.Wrapper;

public class CommandHClip extends Command {
    public CommandHClip() {
        super("hclip", new String[0], "<distance>");
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 1) {
            Wrapper.printChat("\247cNot enough arguments.");
            return;
        }

        if (!MathHelper.isDouble(args[0])) {
            Wrapper.printChat("\247c<distance> must be a number.");
            return;
        }

        double distance = Double.parseDouble(args[0]);
        float[] vector = Wrapper.getPlayer().getMoveVector();
        Wrapper.getPlayer().setPosition(Wrapper.getPlayer().posX + (vector[0] * distance), Wrapper.getPlayer().posY,
                Wrapper.getPlayer().posZ + (vector[1] * distance));
        Wrapper.printChat(String.format("Teleported %f blocks horizontally.", distance));
    }
}
