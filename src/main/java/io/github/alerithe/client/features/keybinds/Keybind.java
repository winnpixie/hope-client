package io.github.alerithe.client.features.keybinds;

import io.github.alerithe.client.features.Feature;

public class Keybind extends Feature {
    private int key;
    private Runnable action;

    public Keybind(String name, String[] aliases, int key, Runnable action) {
        super(name, aliases);
        this.key = key;
        this.action = action;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public Runnable getAction() {
        return action;
    }

    public void setAction(Runnable action) {
        this.action = action;
    }
}
