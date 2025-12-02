package io.github.alerithe.client.features.commands.impl;

import io.github.alerithe.client.Client;
import io.github.alerithe.client.features.commands.Command;
import io.github.alerithe.client.utilities.GameHelper;
import io.github.alerithe.client.utilities.graphics.VisualHelper;

import java.util.Arrays;

public class CommandClient extends Command {
    public CommandClient() {
        super("clientinfo", new String[]{"ci", "client"}, "");
    }

    @Override
    public void execute(String[] args) {
        GameHelper.printChatMessage("\247eClient Information:");
        GameHelper.printChatMessage("Name: \247e" + Client.NAME);
        GameHelper.printChatMessage("Build: \247e" + Client.BUILD);
        GameHelper.printChatMessage("Accent Color: \247e(RGBA) " + Arrays.toString(VisualHelper.toRGBAIntArray(Client.ACCENT_COLOR, true)));
        GameHelper.printChatMessage("Modules: \247e" + Client.MODULE_MANAGER.getChildren().size());
        GameHelper.printChatMessage("Commands: \247e" + Client.COMMAND_MANAGER.getChildren().size());
        GameHelper.printChatMessage("Plugins: \247e" + Client.PLUGIN_MANAGER.getChildren().size());
    }
}
