package io.github.alerithe.client.features.commands.impl;

import io.github.alerithe.client.features.commands.Command;
import io.github.alerithe.client.features.commands.ErrorMessages;
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
            Wrapper.printMessage(ErrorMessages.NOT_ENOUGH_ARGS);
            return;
        }

        try {
            Profile profile = MinecraftAPI.getProfile(args[0]);
            Wrapper.printMessage("\247eProfile Information:");
            Wrapper.printMessage("Username: \247e" + profile.name);
            Wrapper.printMessage("UUID: \247e" + profile.id);
            Wrapper.printMessage("Legacy: \247e" + profile.legacy);
            Wrapper.printMessage("Demo: \247e" + profile.demo);
            Wrapper.printMessage("Has OptiFine Cape? \247e" + hasOptiFineCape(profile.name));
            Wrapper.printMessage("\247eName History:");
            List<Profile.NameEntry> history = MinecraftAPI.getNameHistory(profile);
            Wrapper.printMessage("\247e" + history.size() + " name(s)");
            history.sort(Comparator.comparingLong(entry -> entry.changedToAt));
            history.forEach(entry -> {
                if (entry.changedToAt == 0) {
                    Wrapper.printMessage("\247e-\247r " + entry.name + " \247e(original)");
                } else {
                    Wrapper.printMessage("\247e-\247r " + entry.name + " \247e(" + new Date(entry.changedToAt) + ")");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Wrapper.printMessage(ErrorMessages.format("Error obtaining profile information."));
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
