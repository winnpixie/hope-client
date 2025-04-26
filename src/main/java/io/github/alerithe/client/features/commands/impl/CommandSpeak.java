package io.github.alerithe.client.features.commands.impl;

import io.github.alerithe.client.Client;
import io.github.alerithe.client.features.commands.Command;
import io.github.alerithe.client.features.commands.ErrorMessages;
import io.github.alerithe.client.utilities.Wrapper;

public class CommandSpeak extends Command {
    public CommandSpeak() {
        super("speak", new String[]{"talk"}, "<message>");
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 1) {
            Wrapper.printMessage(ErrorMessages.NOT_ENOUGH_ARGS);
            return;
        }

        Client.NARRATOR.narrate(String.join(" ", args));
    }
}
