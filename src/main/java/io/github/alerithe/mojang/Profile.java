package io.github.alerithe.mojang;

public class Profile {
    public String name;
    public String id;
    public boolean legacy = false;
    public boolean demo = false;

    public static class NameEntry {
        public String name;
        public long changedToAt = 0L;
    }
}
