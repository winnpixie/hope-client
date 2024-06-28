package io.github.alerithe.client.utilities;

public class Timer {
    private long prevMs;

    public Timer() {
        update();
    }

    public boolean hasPassed(long ms) {
        return getElapsed() >= ms;
    }

    public long getElapsed() {
        return getTime() - prevMs;
    }

    public void update() {
        prevMs = getTime();
    }

    public static long getTime() {
        return System.nanoTime() / 1000000;
    }
}
