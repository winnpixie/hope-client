package io.github.alerithe.mojang;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class Profile {
    public String name;
    public String id;
    public boolean legacy = false;
    public boolean demo = false;

    public NameEntry[] getNameHistory() throws Exception {
        return MinecraftAPI.getNameHistory(id);
    }

    public boolean hasCape(CapeProvider provider) {
        switch (provider) {
            case MOJANG:
                // TODO: Implement
                return false;

            case OPTIFINE: {
                HttpURLConnection conn = null;
                try {
                    conn = (HttpURLConnection) new URL(String.format("http://s.optifine.net/capes/%s.png", name)).openConnection();
                    conn.setUseCaches(false);

                    return conn.getResponseCode() == 200;
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (conn != null) conn.disconnect();
                }
            }
        }

        return false;
    }

    public static class NameEntry {
        public String name;
        public long changedToAt = 0L;
    }
}
