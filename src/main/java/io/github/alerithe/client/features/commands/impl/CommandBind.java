package io.github.alerithe.client.features.commands.impl;

import io.github.alerithe.client.Client;
import io.github.alerithe.client.features.commands.Command;
import io.github.alerithe.client.features.commands.ErrorMessages;
import io.github.alerithe.client.features.keybinds.Keybind;
import io.github.alerithe.client.utilities.Wrapper;
import org.lwjgl.input.Keyboard;

public class CommandBind extends Command {
    public CommandBind() {
        super("bind", new String[]{"b"}, "<keybind> <key>");
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            Wrapper.printChat(ErrorMessages.NOT_ENOUGH_ARGS);
            return;
        }

        Keybind keybind = Client.KEYBIND_MANAGER.find(args[0]);
        if (keybind == null) {
            Wrapper.printChat(ErrorMessages.INVALID_TARGET);
            return;
        }

        int key = Keyboard.getKeyIndex(args[1].toUpperCase());
        keybind.setKey(key);
        Wrapper.printChat(String.format("%s is now bound to %s.", keybind.getName(), Keyboard.getKeyName(key)));
    }
}
