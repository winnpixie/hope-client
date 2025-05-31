package io.github.alerithe.client.features.commands;

import io.github.alerithe.client.Client;
import io.github.alerithe.client.events.game.EventChat;
import io.github.alerithe.client.features.FeatureManager;
import io.github.alerithe.client.features.commands.impl.*;
import io.github.alerithe.client.utilities.GameHelper;

import java.util.Arrays;

public class CommandManager extends FeatureManager<Command> {
    @Override
    public void load() {
        add(new CommandBadItem());
        add(new CommandBind());
        add(new CommandEditSign());
        add(new CommandEntityInfo());
        add(new CommandForward());
        add(new CommandHClip());
        add(new CommandHelp());
        add(new CommandModule());
        add(new CommandRejoin());
        add(new CommandSay());
        add(new CommandServerInfo());
        add(new CommandSetName());
        add(new CommandSkull());
        add(new CommandSpeak());
        add(new CommandToggle());
        add(new CommandVClip());
        add(new CommandWhois());

        Client.LOGGER.info(String.format("Registered %d command(s)", getChildren().size()));

        Client.EVENT_BUS.subscribe(EventChat.class, event -> {
            if (event.getMessage().indexOf('.') != 0) return;
            if (event.getMessage().length() < 2) return;

            event.cancel();
            String[] args = event.getMessage().substring(1).split(" ");
            Command command = find(args[0]);
            if (command == null) {
                GameHelper.printChatMessage(ErrorMessages.INVALID_COMMAND);
                return;
            }

            command.execute(args.length > 1 ? Arrays.copyOfRange(args, 1, args.length) : new String[0]);
        });
    }
}
