package io.github.alerithe.client.features.commands.impl;

import io.github.alerithe.client.Client;
import io.github.alerithe.client.features.commands.Command;

public class CommandSpeak extends Command {
    public CommandSpeak() {
        super("speak", new String[0], "<message>");
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 1) return;

        Client.TTS_MANAGER.queueText(String.join(" ", args));
    }
}
