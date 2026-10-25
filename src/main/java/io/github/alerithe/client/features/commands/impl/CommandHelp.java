package io.github.alerithe.client.features.commands.impl;

import io.github.alerithe.client.Client;
import io.github.alerithe.client.features.commands.Command;
import io.github.alerithe.client.features.commands.ErrorMessages;
import io.github.alerithe.client.utilities.GameHelper;
import io.github.alerithe.client.utilities.MathHelper;

import java.util.Arrays;

public class CommandHelp extends Command {
    private static final int COMMANDS_PER_PAGE = 7;

    public CommandHelp() {
        super("help", new String[]{"?", "commands"}, "[page|command]");
    }

    @Override
    public void execute(String[] args) {
        int page = 1;

        if (args.length > 0) {
            Command command = Client.COMMAND_MANAGER.find(args[0]);

            if (command != null) {
                GameHelper.printChatMessage("\247eHelp for command '" + command.getName() + "':");
                GameHelper.printChatMessage("Usage: ." + command.getName() + " " + command.getUsage());
                GameHelper.printChatMessage("Aliases: " + Arrays.toString(command.getAliases()));
                return;
            } else if (MathHelper.isInteger(args[0])) {
                page = Integer.parseInt(args[0]);
            } else {
                GameHelper.printChatMessage(ErrorMessages.INVALID_ARG_TYPE);
                return;
            }
        }

        int pageCount = MathHelper.ceil(Client.COMMAND_MANAGER.getElements().size() / (float) COMMANDS_PER_PAGE); // 7 COMMANDS PER PAGE
        GameHelper.printChatMessage("\247eCommands (Page " + page + "/" + pageCount + ")");
        GameHelper.printChatMessage("\2477<arg> = Required, [arg] = Optional");
        for (int i = 0; i < COMMANDS_PER_PAGE; i++) {
            int idx = i + ((page - 1) * COMMANDS_PER_PAGE);
            if (idx > Client.COMMAND_MANAGER.getElements().size() - 1) break;

            Command command = Client.COMMAND_MANAGER.getElements().get(idx);
            GameHelper.printChatMessage("\247a> \247r." + command.getName() + " " + command.getUsage());
        }
    }
}
