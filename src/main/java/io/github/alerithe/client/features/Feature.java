package io.github.alerithe.client.features;

public class Feature {
    private String name;
    private String[] aliases;

    public Feature(String name, String... aliases) {
        this.name = name;
        this.aliases = aliases;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getAliases() {
        return aliases;
    }

    public void setAliases(String[] aliases) {
        this.aliases = aliases;
    }
}
