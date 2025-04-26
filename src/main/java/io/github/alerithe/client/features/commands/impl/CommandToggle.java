package io.github.alerithe.client.features.commands.impl;

import io.github.alerithe.client.Client;
import io.github.alerithe.client.features.commands.Command;
import io.github.alerithe.client.features.commands.ErrorMessages;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.utilities.Wrapper;

public class CommandToggle extends Command {
    public CommandToggle() {
        super("toggle", new String[]{"t"}, "<module>");
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 1) {
            Wrapper.printMessage(ErrorMessages.NOT_ENOUGH_ARGS);
            return;
        }

        Module module = Client.MODULE_MANAGER.find(args[0]);
        if (module == null) {
            Wrapper.printMessage(ErrorMessages.INVALID_TARGET);
            return;
        }

        module.toggle();
        Wrapper.printMessage(String.format("%s is now %s.", module.getName(), module.isEnabled() ? "ON" : "OFF"));
    }
}
