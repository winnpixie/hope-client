package io.github.alerithe.client.features.commands;

import io.github.alerithe.client.Client;
import io.github.alerithe.client.events.game.EventChat;
import io.github.alerithe.client.features.FeatureManager;
import io.github.alerithe.client.features.commands.impl.*;
import io.github.alerithe.client.utilities.GameHelper;

public class CommandManager extends FeatureManager<Command> {
    @Override
    public void load() {
        add(new CommandBadItem());
        add(new CommandBind());
        add(new CommandClient());
        add(new CommandEditSign());
        add(new CommandEntityInfo());
        add(new CommandForward());
        add(new CommandFriend());
        add(new CommandHClip());
        add(new CommandHelp());
        add(new CommandIP());
        add(new CommandModule());
        add(new CommandPlugins());
        add(new CommandRejoin());
        add(new CommandSay());
        add(new CommandServerInfo());
        add(new CommandSetName());
        add(new CommandSkull());
        add(new CommandSpeak());
        add(new CommandToggle());
        add(new CommandVClip());
        add(new CommandWhois());

        Client.LOGGER.info("Registered {} command(s)", getElements().size());

        Client.EVENT_BUS.subscribe(EventChat.class, event -> {
            String message = event.getMessage();
            if (message.length() < 2
                    || message.indexOf('.') != 0) {
                return;
            }

            event.cancel();
            String[] tokens = message.substring(1).split(" ");
            Command command = find(tokens[0]);
            if (command == null) {
                GameHelper.printChatMessage(ErrorMessages.INVALID_COMMAND);
                return;
            }

            String[] arguments = new String[tokens.length - 1];
            if (tokens.length > 1) {
                System.arraycopy(tokens, 1, arguments, 0, arguments.length);
            }

            command.execute(arguments);
        });
    }
}
