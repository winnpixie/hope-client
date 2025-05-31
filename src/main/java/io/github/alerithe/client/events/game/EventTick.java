package io.github.alerithe.client.events.game;

public class EventTick {
    private final boolean isInGame;

    public EventTick(boolean isInGame) {
        this.isInGame = isInGame;
    }

    public boolean isInGame() {
        return isInGame;
    }

    public static class Start extends EventTick {
        public Start(boolean isInGame) {
            super(isInGame);
        }
    }

    public static class End extends EventTick {
        public End(boolean isInGame) {
            super(isInGame);
        }
    }
}
