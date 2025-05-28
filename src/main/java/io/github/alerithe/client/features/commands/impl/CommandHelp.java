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
        super("help", new String[]{"?", "commands"}, "[page/command]");
    }

    @Override
    public void execute(String[] args) {
        int page = 1;

        if (args.length > 0) {
            Command command = Client.COMMAND_MANAGER.find(args[0]);

            if (command != null) {
                GameHelper.printChatMessage(String.format("\247eHelp for command '%s':", command.getName()));
                GameHelper.printChatMessage(String.format("Usage: .%s %s", command.getName(), command.getUsage()));
                GameHelper.printChatMessage(String.format("Aliases: %s", Arrays.toString(command.getAliases())));
                return;
            } else if (MathHelper.isInt(args[0])) {
                page = Integer.parseInt(args[0]);
            } else {
                GameHelper.printChatMessage(ErrorMessages.INVALID_ARG_TYPE);
                return;
            }
        }

        int pageCount = MathHelper.ceil(Client.COMMAND_MANAGER.getChildren().size() / (float) COMMANDS_PER_PAGE); // 7 COMMANDS PER PAGE
        GameHelper.printChatMessage(String.format("\247eCommands (Page %d/%d)", page, pageCount));
        GameHelper.printChatMessage("\2477<arg> = Required, [arg] = Optional");
        for (int i = 0; i < COMMANDS_PER_PAGE; i++) {
            int idx = i + ((page - 1) * COMMANDS_PER_PAGE);
            if (idx > Client.COMMAND_MANAGER.getChildren().size() - 1) break;

            Command command = Client.COMMAND_MANAGER.getChildren().get(idx);
            GameHelper.printChatMessage(String.format("\247a> \247r.%s %s", command.getName(), command.getUsage()));
        }
    }
}
