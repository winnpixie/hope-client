package io.github.alerithe.events;

public class CancellableEvent implements Event {
    private boolean cancelled;

    public boolean isCancelled() {
        return cancelled;
    }

    public void cancel() {
        this.cancelled = true;
    }
}
