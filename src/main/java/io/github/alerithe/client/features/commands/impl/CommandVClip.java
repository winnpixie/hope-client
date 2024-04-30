package io.github.alerithe.client.features.commands.impl;

import io.github.alerithe.client.features.commands.Command;
import io.github.alerithe.client.utilities.MathHelper;
import io.github.alerithe.client.utilities.Wrapper;

public class CommandVClip extends Command {
    public CommandVClip() {
        super("vclip", new String[0], "<distance>");
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
        Wrapper.getPlayer().setPosition(Wrapper.getPlayer().posX, Wrapper.getPlayer().posY + distance,
                Wrapper.getPlayer().posZ);
        Wrapper.printChat(String.format("Teleported %f blocks vertically.", distance));
    }
}
