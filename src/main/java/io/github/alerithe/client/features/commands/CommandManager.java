package io.github.alerithe.client.features.commands;

import io.github.alerithe.client.Client;
import io.github.alerithe.client.events.EventChat;
import io.github.alerithe.client.features.FeatureManager;
import io.github.alerithe.client.features.commands.impl.*;
import io.github.alerithe.client.utilities.Wrapper;
import io.github.alerithe.events.EventBus;
import io.github.alerithe.events.EventHandler;

import java.util.Arrays;

public class CommandManager extends FeatureManager<Command> {
    @Override
    public void load() {
        add(new CommandBind());
        add(new CommandBadItem());
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

        Client.LOGGER.info(String.format("Registered %d Commands", getElements().size()));

        EventBus.register(new EventHandler<EventChat>() {
            @Override
            public void handle(EventChat event) {
                if (event.getMessage().indexOf('.') != 0) return;
                if (event.getMessage().length() < 2) return;

                event.setCancelled(true);
                String[] args = event.getMessage().substring(1).split(" ");
                Command command = get(args[0]);
                if (command == null) {
                    Wrapper.printChat(String.format("\247cNo such command '.%s'.", args[0]));
                    return;
                }

                command.execute(args.length > 1 ? Arrays.copyOfRange(args, 1, args.length) : new String[0]);
            }
        });
    }
}
