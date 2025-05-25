package io.github.alerithe.client.events.game;

public class EventTick {
    private final boolean isInGame;

    public EventTick(boolean isInGame) {
        this.isInGame = isInGame;
    }

    public boolean isInGame() {
        return isInGame;
    }
}
