package io.github.alerithe.mojang;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonSyntaxException;
import io.github.alerithe.http.HttpClient;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

public class MinecraftAPI {
    private static final Gson GSON = new GsonBuilder().create();
    private static final Comparator<Profile.NameEntry> entryComparator = Comparator.comparingLong(entry -> entry.changedToAt);

    public static final String API_BASE = "https://api.mojang.com";

    private MinecraftAPI() {
    }

    public static Profile getProfile(String username) throws IOException, JsonSyntaxException {
        String body = HttpClient.send(
                HttpClient.newRequest()
                        .url(API_BASE + "/users/profiles/minecraft/" + username)
                        .header("User-Agent", "mojank (profile)")
                        .build()
        ).getBodyAsString();

        return GSON.fromJson(body, Profile.class);
    }

    public static Profile.NameEntry[] getNameHistory(String uuid) throws IOException, JsonSyntaxException {
        String body = HttpClient.send(
                HttpClient.newRequest()
                        .url(API_BASE + "/user/profiles/" + uuid + "/names")
                        .header("User-Agent", "mojank (history)")
                        .build()
        ).getBodyAsString();

        JsonArray entries = GSON.fromJson(body, JsonArray.class);
        Profile.NameEntry[] history = new Profile.NameEntry[entries.size()];

        for (int i = 0; i < history.length; i++) {
            history[i] = GSON.fromJson(entries.get(i),
                    Profile.NameEntry.class);
        }

        Arrays.sort(history, entryComparator);
        return history;
    }
}
