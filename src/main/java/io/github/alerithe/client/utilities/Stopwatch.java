package io.github.alerithe.client.utilities;

public class Stopwatch {
    private long instant;

    public Stopwatch() {
        update();
    }

    public boolean hasPassed(long duration) {
        return getElapsed() >= duration;
    }

    public long getElapsed() {
        return getNow() - instant;
    }

    public void update() {
        instant = getNow();
    }

    public static long getNow() {
        return System.nanoTime() / 1000000;
    }
}
