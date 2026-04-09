package io.github.alerithe.mojang;

import com.google.gson.JsonSyntaxException;
import io.github.alerithe.http.HttpClient;
import io.github.alerithe.http.HttpResponse;

import java.io.IOException;

public class Profile {
    public String name;
    public String id;
    public boolean legacy = false;
    public boolean demo = false;

    public NameEntry[] getNameHistory() throws IOException, JsonSyntaxException {
        return MinecraftAPI.getNameHistory(id);
    }

    public boolean hasCape(CapeProvider provider) {
        switch (provider) {
            case MOJANG:
                // TODO: Implement
                return false;
            case OPTIFINE:
                try {
                    HttpResponse response = HttpClient.send(
                            HttpClient.newRequest()
                                    .url(String.format("http://s.optifine.net/capes/%s.png", name))
                                    .build());
                    return response.getCode() == 200;
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                    return false;
                }
            default:
                return false;
        }
    }

    public static class NameEntry {
        public String name;
        public long changedToAt = 0L;
    }
}
