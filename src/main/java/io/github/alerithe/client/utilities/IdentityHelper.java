package io.github.alerithe.client.utilities;


import com.sun.management.OperatingSystemMXBean;
import io.github.winnpixie.http4j.client.HttpClient;
import io.github.winnpixie.http4j.shared.throwables.HttpException;

import java.awt.*;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileStore;
import java.nio.file.FileSystems;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

// This class is serves no purpose unless I go paid/private.
public class IdentityHelper {
    private static final HttpClient HTTP = HttpClient.newClient();

    private static String rawId;
    private static String hashedId;

    private IdentityHelper() {
    }

    // TODO: Pick out information to deem as personally identifiable
    public static String getRawId() {
        if (rawId == null) {
            StringBuilder metadata = new StringBuilder();

            OperatingSystemMXBean os = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
            RuntimeMXBean rt = ManagementFactory.getRuntimeMXBean();
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

            // CPU (arch is OS-dependent, but typically the same)
            metadata.append(os.getArch())
                    .append(';')
                    .append(os.getAvailableProcessors())
                    .append('/');

            // OS
            metadata.append(os.getName())
                    .append(';')
                    .append(os.getVersion())
                    .append('/');

            // Memory
            metadata.append(os.getTotalPhysicalMemorySize())
                    .append(';')
                    .append(os.getTotalSwapSpaceSize())
                    .append('/');

            // Runtime - VM
            metadata.append(rt.getVmVendor())
                    .append(';')
                    .append(rt.getVmName())
                    .append(';')
                    .append(rt.getVmVersion())
                    .append('/');
            // Runtime - Spec
            metadata.append(rt.getSpecVendor())
                    .append(';')
                    .append(rt.getSpecName())
                    .append(';')
                    .append(rt.getSpecVersion())
                    .append('/');
            // Runtime - Management
            metadata.append(rt.getManagementSpecVersion())
                    .append('/');
            // Runtime - Paths
            metadata.append(rt.getBootClassPath())
                    .append(';')
                    .append(rt.getClassPath())
                    .append(';')
                    .append(rt.getLibraryPath())
                    .append('/');
            // Runtime - Properties
            for (Map.Entry<String, String> property : rt.getSystemProperties().entrySet()) {
                metadata.append(property.getKey())
                        .append(';')
                        .append(property.getValue())
                        .append('/');
            }

            // Files
            for (FileStore store : FileSystems.getDefault().getFileStores()) {
                try {
                    metadata.append(store)
                            .append(';')
                            .append(store.getTotalSpace())
                            .append('/');
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            // Graphics
            for (GraphicsDevice gd : ge.getScreenDevices()) {
                for (DisplayMode dm : gd.getDisplayModes()) {
                    metadata.append(dm.getWidth())
                            .append('x')
                            .append(dm.getHeight())
                            .append(';')
                            .append(dm.getRefreshRate())
                            .append("hz;")
                            .append(dm.getBitDepth())
                            .append("bpp")
                            .append('/');
                }
            }

            // IP
            metadata.append(getPublicIPv4());

            rawId = metadata.toString();
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
        if (hashedId == null) {
            String raw = getRawId();
            MessageDigest md = null;
            try {
                md = MessageDigest.getInstance("SHA-256");
                md.reset();
                byte[] hash = md.digest(raw.getBytes(StandardCharsets.UTF_8));

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

                hashedId = raw;
            } finally {
                if (md != null) {
                    md.reset();
                }
            }
        }

        return hashedId;
    }
}
