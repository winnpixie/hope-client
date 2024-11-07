package io.github.alerithe.client.features.commands.impl;

import io.github.alerithe.client.features.commands.Command;
import io.github.alerithe.client.features.commands.ErrorMessages;
import io.github.alerithe.client.utilities.Wrapper;
import net.minecraft.network.play.client.C01PacketChatMessage;

public class CommandSay extends Command {
    public CommandSay() {
        super("say", new String[0], "<message>");
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 1) {
            Wrapper.printChat(ErrorMessages.NOT_ENOUGH_ARGS);
            return;
        }

        Wrapper.sendPacket(new C01PacketChatMessage(String.join(" ", args)));
    }
}
