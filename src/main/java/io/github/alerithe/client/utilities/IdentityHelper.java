package io.github.alerithe.client.utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

// This class is serves no purpose unless I go paid/private.
public class IdentityHelper {
    private static String RAW_ID = "";
    private static String HASHED_ID = "";

    // TODO: Pick out information to deem as personally identifiable
    public static String getRawId() {
        if (RAW_ID.isEmpty()) {
            RAW_ID = System.getProperty("os.name")
                    + ';' + System.getProperty("os.arch")
                    + '/' + System.getProperty("user.name")
                    + ';' + System.getProperty("user.home")
                    + '/' + System.getProperty("java.version")
                    + ';' + System.getProperty("java.home")
                    + '/' + getPublicIPv4();
        }

        return RAW_ID;
    }

    public static String getPublicIPv4() {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL("https://checkip.amazonaws.com/").openConnection();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                return reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "127.0.0.1";
        } finally {
            if (connection != null) connection.disconnect();
        }
    }

    public static String getId() {
        if (HASHED_ID.isEmpty()) {
            MessageDigest md = null;
            try {
                md = MessageDigest.getInstance("SHA-256");
                md.reset();
                byte[] hash = md.digest(getRawId().getBytes(StandardCharsets.UTF_8));
    
                StringBuilder builder = new StringBuilder();
                for (byte b : hash) {
                    String hex = Integer.toString(b & 0xFF, 16);
                    if (hex.length() < 2) hex = '0' + hex;
    
                    builder.append(hex);
                }
    
                return builder.toString();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                HASHED_ID = getRawId();
            } finally {
                if (md != null) md.reset();
            }
        }
        
        return HASHED_ID;
    }
}
