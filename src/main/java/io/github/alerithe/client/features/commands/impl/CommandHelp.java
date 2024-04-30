package io.github.alerithe.client.features.commands.impl;

import io.github.alerithe.client.Client;
import io.github.alerithe.client.features.commands.Command;
import io.github.alerithe.client.utilities.MathHelper;
import io.github.alerithe.client.utilities.Wrapper;

import java.util.Arrays;

public class CommandHelp extends Command {
    public CommandHelp() {
        super("help", new String[]{"?", "commands"}, "[page/command]");
    }

    @Override
    public void execute(String[] args) {
        int maxPages = (Client.COMMAND_MANAGER.getElements().size() - 1) / 7;
        int page = 0;

        if (args.length > 0) {
            Command command = Client.COMMAND_MANAGER.get(args[0]);

            if (command != null) {
                Wrapper.printChat(String.format("\247eHelp for command '%s':", command.getName()));
                Wrapper.printChat(String.format("Usage: .%s %s", command.getName(), command.getUsage()));
                Wrapper.printChat(String.format("Aliases: %s", Arrays.toString(command.getAliases())));
                return;
            } else if (MathHelper.isInt(args[0])) {
                page = Integer.parseInt(args[0]);
                if (page < 1) {
                    page = 1;
                }
                if (page > maxPages + 1) {
                    page = maxPages + 1;
                }
                page -= 1;
            } else {
                Wrapper.printChat("\247cInvalid Argument Type.");
            }
        }

        int endIndex = Math.min((page + 1) * 7, Client.COMMAND_MANAGER.getElements().size());
        Wrapper.printChat(String.format("\247eCommands (Page %d/%d)", page + 1, maxPages + 1));
        Wrapper.printChat("\2477<arg> = Required, [arg] = Optional");
        for (int i = page * 7; i < endIndex; ++i) {
            Command command = Client.COMMAND_MANAGER.getElements().get(i);
            Wrapper.printChat(String.format("\247a> \247r.%s %s", command.getName(), command.getUsage()));
        }
    }
}
