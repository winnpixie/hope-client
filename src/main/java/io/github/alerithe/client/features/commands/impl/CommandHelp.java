package io.github.alerithe.client.features.commands.impl;

import io.github.alerithe.client.Client;
import io.github.alerithe.client.features.commands.Command;
import io.github.alerithe.client.features.commands.ErrorMessages;
import io.github.alerithe.client.utilities.MathHelper;
import io.github.alerithe.client.utilities.Wrapper;

import java.util.Arrays;

public class CommandHelp extends Command {
    public CommandHelp() {
        super("help", new String[]{"?", "commands"}, "[page/command]");
    }

    @Override
    public void execute(String[] args) {
        int page = 1;

        if (args.length > 0) {
            Command command = Client.COMMAND_MANAGER.find(args[0]);

            if (command != null) {
                Wrapper.printMessage(String.format("\247eHelp for command '%s':", command.getName()));
                Wrapper.printMessage(String.format("Usage: .%s %s", command.getName(), command.getUsage()));
                Wrapper.printMessage(String.format("Aliases: %s", Arrays.toString(command.getAliases())));
                return;
            } else if (MathHelper.isInt(args[0])) {
                page = Integer.parseInt(args[0]);
            } else {
                Wrapper.printMessage(ErrorMessages.INVALID_ARG_TYPE);
                return;
            }
        }

        final int PER_PAGE = 7;
        int pageCount = (Client.COMMAND_MANAGER.getElements().size() - 1) / PER_PAGE; // 7 COMMANDS PER PAGE
        Wrapper.printMessage(String.format("\247eCommands (Page %d/%d)", page, pageCount + 1));
        Wrapper.printMessage("\2477<arg> = Required, [arg] = Optional");
        for (int i = 0; i < PER_PAGE; i++) {
            int idx = i + ((page - 1) * PER_PAGE);
            if (idx > Client.COMMAND_MANAGER.getElements().size() - 1) break;

            Command command = Client.COMMAND_MANAGER.getElements().get(idx);
            Wrapper.printMessage(String.format("\247a> \247r.%s %s", command.getName(), command.getUsage()));
        }
    }
}
