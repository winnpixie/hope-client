package io.github.alerithe.client.features.commands.impl;

import io.github.alerithe.client.features.commands.Command;
import io.github.alerithe.client.features.commands.ErrorMessages;
import io.github.alerithe.client.utilities.GameHelper;
import net.minecraft.util.Session;

public class CommandSetName extends Command {
    public CommandSetName() {
        super("setname", new String[]{"name"}, "<name>");
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 1) {
            GameHelper.printChatMessage(ErrorMessages.NOT_ENOUGH_ARGS);
            return;
        }

        if (!args[0].equalsIgnoreCase(GameHelper.getGame().getSession().getUsername())) {
            GameHelper.printChatMessage(ErrorMessages.format("<name> must match your current name (case-insensitive)."));
            return;
        }

        Session old = GameHelper.getGame().getSession();
        GameHelper.getGame().setSession(new Session(args[0], old.getPlayerID(), old.getToken(), old.getSessionType().getName()));
        GameHelper.printChatMessage(String.format("Your name is now %s, reconnect for it to take effect.", args[0]));
    }
}
