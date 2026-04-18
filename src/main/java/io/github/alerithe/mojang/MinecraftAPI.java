package io.github.alerithe.mojang;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonSyntaxException;
import io.github.winnpixie.http4j.client.HttpClient;
import io.github.winnpixie.http4j.shared.throwables.HttpException;

import java.util.Arrays;
import java.util.Comparator;

public class MinecraftAPI {
    private static final HttpClient HTTP = HttpClient.newClient();
    private static final Gson GSON = new GsonBuilder().create();
    private static final Comparator<Profile.NameEntry> entryComparator = Comparator.comparingLong(entry -> entry.changedToAt);

    public static final String API_BASE = "https://api.mojang.com";

    private MinecraftAPI() {
    }

    public static Profile getProfile(String username) throws HttpException, JsonSyntaxException {
        String body = HTTP.send(HTTP.newRequest()
                .setUrl(API_BASE + "/users/profiles/minecraft/" + username)
                .setHeader("User-Agent", "mojank (profile)")
                .build()
        ).getBodyAsString();

        return GSON.fromJson(body, Profile.class);
    }

    public static Profile.NameEntry[] getNameHistory(String uuid) throws HttpException, JsonSyntaxException {
        String body = HTTP.send(HTTP.newRequest()
                .setUrl(API_BASE + "/user/profiles/" + uuid + "/names")
                .setHeader("User-Agent", "mojank (history)")
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
