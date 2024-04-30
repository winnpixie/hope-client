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
        return getNow() - prevMs;
    }

    public void update() {
        prevMs = getNow();
    }

    public static long getNow() {
        return System.nanoTime() / 1000000;
    }
}
