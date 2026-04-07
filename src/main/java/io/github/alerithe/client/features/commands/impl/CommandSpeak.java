package io.github.alerithe.client.features.commands.impl;

import io.github.alerithe.client.Client;
import io.github.alerithe.client.features.commands.Command;
import io.github.alerithe.client.features.commands.ErrorMessages;
import io.github.alerithe.client.utilities.GameHelper;

public class CommandSpeak extends Command {
    public CommandSpeak() {
        super("speak", new String[0], "<statement>");
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 1) {
            GameHelper.printChatMessage(ErrorMessages.NOT_ENOUGH_ARGS);
            return;
        }

        String statement = String.join(" ", args);
        Client.SPEECH_ENGINE.queue(statement);

        GameHelper.printChatMessage(String.format("\247eAdded to queue:\247r %s", statement));
    }
}
