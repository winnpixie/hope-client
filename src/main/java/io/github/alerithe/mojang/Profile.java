package io.github.alerithe.mojang;

import com.google.gson.JsonSyntaxException;
import io.github.winnpixie.http4j.client.HttpClient;
import io.github.winnpixie.http4j.shared.throwables.HttpException;

public class Profile {
    public String name;
    public String id;
    public boolean legacy = false;
    public boolean demo = false;

    public NameEntry[] getNameHistory() throws HttpException, JsonSyntaxException {
        return MinecraftAPI.getNameHistory(id);
    }

    public boolean hasCape(CapeProvider provider) {
        HttpClient http = HttpClient.newClient(1);

        switch (provider) {
            case MOJANG:
                // TODO: Implement
                return false;
            case OPTIFINE:
                try {
                    return http.send(http.newRequest()
                                    .setUrl(String.format("http://s.optifine.net/capes/%s.png", name))
                                    .build())
                            .getStatus().getCode() == 200;
                } catch (HttpException ioe) {
                    // ignore throw
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
