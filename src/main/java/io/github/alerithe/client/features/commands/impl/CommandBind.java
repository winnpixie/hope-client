package io.github.alerithe.client.features.commands.impl;

import io.github.alerithe.client.Client;
import io.github.alerithe.client.features.commands.Command;
import io.github.alerithe.client.features.commands.ErrorMessages;
import io.github.alerithe.client.features.keybinds.Keybind;
import io.github.alerithe.client.utilities.GameHelper;
import org.lwjgl.input.Keyboard;

public class CommandBind extends Command {
    public CommandBind() {
        super("bind", new String[]{"b"}, "<keybind> <key>");
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            GameHelper.printChatMessage(ErrorMessages.NOT_ENOUGH_ARGS);
            return;
        }

        Keybind keybind = Client.KEYBIND_MANAGER.find(args[0]);
        if (keybind == null) {
            GameHelper.printChatMessage(ErrorMessages.INVALID_TARGET);
            return;
        }

        int key = Keyboard.getKeyIndex(args[1].toUpperCase());
        keybind.setKey(key);
        GameHelper.printChatMessage(String.format("%s is now bound to %s.", keybind.getName(), Keyboard.getKeyName(key)));
    }
}
