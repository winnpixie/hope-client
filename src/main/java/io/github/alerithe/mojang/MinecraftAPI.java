package io.github.alerithe.mojang;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MinecraftAPI {
    public static final String API_URL = "https://api.mojang.com";
    private static final Gson gson = new GsonBuilder().create();

    public static Profile getProfile(String username) throws Exception {
        String body = String.join("\n", get(API_URL + "/users/profiles/minecraft/" + username));
        return gson.fromJson(body, Profile.class);
    }

    public static List<Profile.NameEntry> getNameHistory(Profile profile) throws Exception {
        return getNameHistory(profile.id);
    }

    private static List<Profile.NameEntry> getNameHistory(String uuid) throws Exception {
        List<Profile.NameEntry> entries = new ArrayList<>();
        String body = String.join("\n", get(API_URL + "/user/profiles/" + uuid + "/names"));
        JsonArray array = gson.fromJson(body, JsonArray.class);
        array.forEach(elem -> {
            JsonObject obj = elem.getAsJsonObject();
            Profile.NameEntry entry = new Profile.NameEntry();
            entry.name = obj.get("name").getAsString();
            entry.changedToAt = (obj.has("changedToAt") ? obj.get("changedToAt").getAsLong() : 0L);
            entries.add(entry);
        });

        return entries;
    }

    private static List<String> get(String url) throws Exception {
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) new URL(url).openConnection();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                return reader.lines().collect(Collectors.toList());
            }
        } finally {
            if (conn != null) conn.disconnect();
        }
    }
}
