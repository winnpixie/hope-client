package io.github.alerithe.client.features.commands.impl;

import io.github.alerithe.client.Client;
import io.github.alerithe.client.features.commands.Command;
import io.github.alerithe.client.features.commands.ErrorMessages;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.utilities.GameHelper;

public class CommandToggle extends Command {
    public CommandToggle() {
        super("toggle", new String[]{"t"}, "<module>");
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 1) {
            GameHelper.printChatMessage(ErrorMessages.NOT_ENOUGH_ARGS);
            return;
        }

        Module module = Client.MODULE_MANAGER.find(args[0]);
        if (module == null) {
            GameHelper.printChatMessage(ErrorMessages.INVALID_TARGET);
            return;
        }

        module.toggle();
        GameHelper.printChatMessage(String.format("%s is now %s.", module.getName(), module.isEnabled() ? "ON" : "OFF"));
    }
}
