package io.github.alerithe.client.features.commands;

import io.github.alerithe.client.features.Feature;

public abstract class Command extends Feature {
    private final String usage;

    public Command(String name, String[] aliases, String usage) {
        super(name, aliases);
        this.usage = usage;
    }

    public String getUsage() {
        return usage;
    }

    public abstract void execute(String[] args);
}
