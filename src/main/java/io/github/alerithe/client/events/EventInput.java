package io.github.alerithe.client.events;

import io.github.alerithe.events.CancellableEvent;

public class EventInput extends CancellableEvent {
    public static class KeyPress {
        private final int key;

        public KeyPress(int key) {
            this.key = key;
        }

        public int getKey() {
            return key;
        }
    }

    public static class LeftClick extends EventInput {
    }

    public static class RightClick extends EventInput {
        private int delay;

        public RightClick(int delay) {
            this.delay = delay;
        }

        public int getDelay() {
            return delay;
        }

        public void setDelay(int delay) {
            this.delay = delay;
        }
    }

    public static class MiddleClick extends EventInput {
    }
}
