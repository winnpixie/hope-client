package io.github.alerithe.http;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class HttpResponse {
    private final int code;
    private final String status;
    private final Map<String, List<String>> headers;
    private final byte[] body;

    public HttpResponse(int code, String status, Map<String, List<String>> headers, byte[] body) {
        this.code = code;
        this.status = status;
        this.headers = headers;
        this.body = body;
    }

    public int getCode() {
        return code;
    }

    public String getStatus() {
        return status;
    }

    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    public byte[] getBody() {
        return body;
    }

    public String getBodyAsString() {
        return getBodyAsString(StandardCharsets.UTF_8);
    }

    public String getBodyAsString(Charset charset) {
        return new String(body, charset);
    }
}
