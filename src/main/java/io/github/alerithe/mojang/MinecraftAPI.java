package io.github.alerithe.mojang;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Comparator;

public class MinecraftAPI {
    private static final Gson GSON = new GsonBuilder().create();

    public static final String API_BASE = "https://api.mojang.com";

    public static Profile getProfile(String username) throws Exception {
        return GSON.fromJson(getAsString(API_BASE + "/users/profiles/minecraft/" + username),
                Profile.class);
    }

    public static Profile.NameEntry[] getNameHistory(String uuid) throws Exception {
        JsonArray entries = GSON.fromJson(getAsString(API_BASE + "/user/profiles/" + uuid + "/names"),
                JsonArray.class);
        int count = entries.size();
        Profile.NameEntry[] history = new Profile.NameEntry[count];

        for (int i = 0; i < count; i++) {
            history[i] = GSON.fromJson(entries.get(i),
                    Profile.NameEntry.class);
        }

        Arrays.sort(history, Comparator.comparingLong(entry -> entry.changedToAt));
        return history;
    }

    private static String getAsString(String url) throws Exception {
        return new String(get(URI.create(url).toURL()), StandardCharsets.UTF_8);
    }

    private static byte[] get(URL url) throws Exception {
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("User-Agent", "mojang-api");

            try (InputStream is = conn.getInputStream()) {
                ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[8192];
                int read;

                while ((read = is.read(buffer)) != -1) byteStream.write(buffer, 0, read);

                return byteStream.toByteArray();
            }
        } finally {
            if (conn != null) conn.disconnect();
        }
    }
}
