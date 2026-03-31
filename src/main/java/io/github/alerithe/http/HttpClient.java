package io.github.alerithe.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

public class HttpClient {
    public static HttpRequest.Builder newRequest() {
        return new HttpRequest.Builder();
    }

    public static HttpResponse send(HttpRequest request) throws IOException {
        HttpURLConnection connection = null;

        try {
            connection = (HttpURLConnection) request.getUrl().openConnection(request.getProxy());
            connection.setUseCaches(request.isUseCaches());
            connection.setInstanceFollowRedirects(request.isFollowRedirects());
            connection.setRequestMethod(request.getMethod().name());

            request.getHeaders().forEach(connection::setRequestProperty);

            try (InputStream input = connection.getInputStream()) {
                return new HttpResponse(
                        connection.getResponseCode(),
                        connection.getResponseMessage(),
                        connection.getHeaderFields(),
                        readFully(input));
            }
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private static byte[] readFully(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[8192];
        int read;
        while ((read = input.read(buffer)) != -1) {
            output.write(buffer, 0, read);
        }

        return output.toByteArray();
    }
}
