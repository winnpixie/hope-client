package io.github.alerithe.client.events.bus;

public class CancellableEvent implements Event {
    private boolean cancelled;

    public boolean isCancelled() {
        return cancelled;
    }

    public void cancel() {
        this.cancelled = true;
    }
}
