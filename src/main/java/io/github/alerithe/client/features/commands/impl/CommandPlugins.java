package io.github.alerithe.client.features.commands.impl;

import io.github.alerithe.client.Client;
import io.github.alerithe.client.features.commands.Command;
import io.github.alerithe.client.features.plugins.Plugin;
import io.github.alerithe.client.utilities.GameHelper;

import java.util.List;
import java.util.stream.Collectors;

public class CommandPlugins extends Command {
    public CommandPlugins() {
        super("plugins", new String[]{"pl"}, "");
    }

    @Override
    public void execute(String[] args) {
        List<Plugin> plugins = Client.PLUGIN_MANAGER.getElements();
        GameHelper.printChatMessage(String.format("\247ePlugins (%d)", plugins.size()));
        GameHelper.printChatMessage(plugins.stream()
                .map(plugin -> String.format("\247f%s \2477v%s", plugin.getName(), plugin.getManifest().getVersion()))
                .collect(Collectors.joining("\2478, ")));
    }
}
