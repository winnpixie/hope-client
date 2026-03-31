package io.github.alerithe.http;

import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    private final URL url;
    private final HttpMethod method;
    private final Map<String, String> headers;
    private final Proxy proxy;
    private final boolean useCaches;
    private final boolean followRedirects;

    public HttpRequest(Builder builder) {
        this.url = builder.url;
        this.method = builder.method;
        this.headers = builder.headers;
        this.proxy = builder.proxy;
        this.useCaches = builder.useCaches;
        this.followRedirects = builder.followRedirects;
    }

    public URL getUrl() {
        return url;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Proxy getProxy() {
        return proxy;
    }

    public boolean isUseCaches() {
        return useCaches;
    }

    public boolean isFollowRedirects() {
        return followRedirects;
    }

    public static class Builder {
        private URL url;
        private HttpMethod method = HttpMethod.GET;
        private final Map<String, String> headers = new HashMap<>();
        private Proxy proxy = Proxy.NO_PROXY;
        private boolean useCaches = false;
        private boolean followRedirects = true;

        public Builder url(String url) throws MalformedURLException {
            return uri(URI.create(url));
        }

        public Builder uri(URI uri) throws MalformedURLException {
            return url(uri.toURL());
        }

        public Builder url(URL url) {
            this.url = url;

            return this;
        }

        public Builder method(HttpMethod method) {
            this.method = method;

            return this;
        }

        public Builder header(String key, String value) {
            if (value == null) {
                headers.remove(key);
            } else {
                headers.put(key, value);
            }

            return this;
        }

        public Builder proxy(Proxy proxy) {
            this.proxy = proxy;

            return this;
        }

        public Builder useCaches(boolean caches) {
            this.useCaches = caches;

            return this;
        }

        public Builder followRedirects(boolean redirects) {
            this.followRedirects = redirects;

            return this;
        }

        public HttpRequest build() {
            return new HttpRequest(this);
        }
    }
}
