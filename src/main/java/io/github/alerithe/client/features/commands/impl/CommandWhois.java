package io.github.alerithe.client.features.commands.impl;

import io.github.alerithe.client.features.commands.Command;
import io.github.alerithe.client.features.commands.ErrorMessages;
import io.github.alerithe.client.utilities.GameHelper;
import io.github.alerithe.mojang.MinecraftAPI;
import io.github.alerithe.mojang.Profile;
import org.lwjgl.Sys;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;

public class CommandWhois extends Command {
    public CommandWhois() {
        super("whois", new String[]{"profile", "namehistory", "names", "nh"}, "<username>");
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 1) {
            GameHelper.printChatMessage(ErrorMessages.NOT_ENOUGH_ARGS);
            return;
        }

        try {
            Profile profile = MinecraftAPI.getProfile(args[0]);
            GameHelper.printChatMessage("\247eProfile Information:");
            GameHelper.printChatMessage("Username: \247e" + profile.name);
            GameHelper.printChatMessage("UUID: \247e" + profile.id);
            GameHelper.printChatMessage("Legacy: \247e" + profile.legacy);
            GameHelper.printChatMessage("Demo: \247e" + profile.demo);
            GameHelper.printChatMessage("Has OptiFine Cape? \247e" + hasOptiFineCape(profile.name));

            GameHelper.printChatMessage("\247eName History:");
            Profile.NameEntry[] history = profile.getNameHistory();
            GameHelper.printChatMessage("\247e" + history.length + " name(s)");
            for (Profile.NameEntry entry : history) {
                if (entry.changedToAt == 0) {
                    GameHelper.printChatMessage("\247e-\247r " + entry.name + " \247e(original)");
                } else {
                    GameHelper.printChatMessage("\247e-\247r " + entry.name + " \247e(" + new Date(entry.changedToAt) + ")");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            GameHelper.printChatMessage(ErrorMessages.format("Error obtaining profile information."));
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
