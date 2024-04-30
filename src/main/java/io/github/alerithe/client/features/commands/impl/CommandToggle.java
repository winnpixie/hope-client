package io.github.alerithe.client.features.commands.impl;

import io.github.alerithe.client.Client;
import io.github.alerithe.client.features.commands.Command;
import io.github.alerithe.client.features.modules.Module;
import io.github.alerithe.client.utilities.Wrapper;

public class CommandToggle extends Command {
    public CommandToggle() {
        super("toggle", new String[]{"t"}, "<module>");
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 1) {
            Wrapper.printChat("\247cNot enough arguments.");
            return;
        }

        Module module = Client.MODULE_MANAGER.get(args[0]);
        if (module == null) {
            Wrapper.printChat(String.format("\247cNo such module '%s'.", args[0]));
            return;
        }

        module.toggle();
        Wrapper.printChat(String.format("%s is now %s.", module.getName(), module.isEnabled() ? "ON" : "OFF"));
    }
}
