package io.github.alerithe.client.utilities;


import io.github.winnpixie.http4j.client.HttpClient;
import io.github.winnpixie.http4j.shared.throwables.HttpException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

// This class is serves no purpose unless I go paid/private.
public class IdentityHelper {
    private static final HttpClient HTTP = HttpClient.newClient();

    private static String rawId = "";
    private static String hashedId = "";

    private IdentityHelper() {
    }

    // TODO: Pick out information to deem as personally identifiable
    public static String getRawId() {
        if (rawId.isEmpty()) {
            rawId = System.getProperty("os.name")
                    + ';' + System.getProperty("os.arch")
                    + '/' + System.getProperty("user.name")
                    + ';' + System.getProperty("user.home")
                    + '/' + System.getProperty("java.version")
                    + ';' + System.getProperty("java.home")
                    + '/' + getPublicIPv4();
        }

        return rawId;
    }

    public static String getPublicIPv4() {
        try {
            return HTTP.send(HTTP.newRequest()
                    .setUrl("https://checkip.amazonaws.com/")
                    .setHeader("User-Agent", "identity")
                    .build()
            ).getBodyAsString().split("\n")[0];
        } catch (HttpException he) {
            // ignore throw
            return "127.0.0.1";
        }
    }

    public static String getId() {
        if (hashedId.isEmpty()) {
            MessageDigest md = null;
            try {
                md = MessageDigest.getInstance("SHA-256");
                md.reset();
                byte[] hash = md.digest(getRawId().getBytes(StandardCharsets.UTF_8));

                StringBuilder builder = new StringBuilder();
                for (byte b : hash) {
                    String hex = Integer.toString(b & 0xFF, 16);
                    if (hex.length() < 2) {
                        builder.append('0');
                    }

                    builder.append(hex);
                }

                hashedId = builder.toString();
            } catch (NoSuchAlgorithmException nsae) {
                nsae.printStackTrace();

                hashedId = getRawId();
            } finally {
                if (md != null) {
                    md.reset();
                }
            }
        }

        return hashedId;
    }
}
