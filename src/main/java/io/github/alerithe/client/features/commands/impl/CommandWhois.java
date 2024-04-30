package io.github.alerithe.client.features.commands.impl;

import io.github.alerithe.client.features.commands.Command;
import io.github.alerithe.client.utilities.Wrapper;
import io.github.alerithe.mojang.MinecraftAPI;
import io.github.alerithe.mojang.Profile;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class CommandWhois extends Command {
    public CommandWhois() {
        super("whois", new String[]{"profile", "namehistory", "names", "nh"}, "<username>");
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 1) {
            Wrapper.printChat("\247cNot enough arguments.");
            return;
        }

        try {
            Profile profile = MinecraftAPI.getProfile(args[0]);
            Wrapper.printChat("\247eProfile Information:");
            Wrapper.printChat("Username: \247e" + profile.name);
            Wrapper.printChat("UUID: \247e" + profile.id);
            Wrapper.printChat("Legacy: \247e" + profile.legacy);
            Wrapper.printChat("Demo: \247e" + profile.demo);
            Wrapper.printChat("Has OptiFine Cape? \247e" + hasOptiFineCape(profile.name));
            Wrapper.printChat("\247eName History:");
            List<Profile.NameEntry> history = MinecraftAPI.getNameHistory(profile);
            Wrapper.printChat("\247e" + history.size() + " name(s)");
            history.sort(Comparator.comparingLong(entry -> entry.changedToAt));
            history.forEach(entry -> {
                if (entry.changedToAt == 0) {
                    Wrapper.printChat("\247e-\247r " + entry.name + " \247e(original)");
                } else {
                    Wrapper.printChat("\247e-\247r " + entry.name + " \247e(" + new Date(entry.changedToAt) + ")");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Wrapper.printChat("\2474Error obtaining profile information.");
        }
    }

    private boolean hasOptiFineCape(String username) {
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) new URL(String.format("http://s.optifine.net/capes/%s.png", username)).openConnection();
            conn.setUseCaches(false);

            return conn.getResponseCode() == 200;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) conn.disconnect();
        }

        return false;
    }
}
