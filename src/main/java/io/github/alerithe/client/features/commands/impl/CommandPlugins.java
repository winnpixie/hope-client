package io.github.alerithe.client.features.commands.impl;

import io.github.alerithe.client.Client;
import io.github.alerithe.client.features.commands.Command;
import io.github.alerithe.client.features.plugins.Plugin;
import io.github.alerithe.client.utilities.GameHelper;

import java.util.List;

public class CommandPlugins extends Command {
    public CommandPlugins() {
        super("plugins", new String[]{"pl"}, "");
    }

    @Override
    public void execute(String[] args) {
        List<Plugin> plugins = Client.PLUGIN_MANAGER.getElements();
        int pluginCount = plugins.size();
        GameHelper.printChatMessage(String.format("\247ePlugins (%d)", pluginCount));

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < pluginCount; i++) {
            Plugin plugin = plugins.get(i);
            builder.append("\247f")
                    .append(plugin.getName())
                    .append(" \2477v")
                    .append(plugin.getManifest().getVersion());

            if (i + 1 < pluginCount) {
                builder.append("\2478, ");
            }
        }

        GameHelper.printChatMessage(builder.toString());
    }
}
