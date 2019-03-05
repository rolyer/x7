package io.xream.x7.reyc.internal;

import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(
        prefix = "http"
)
public class HttpClientProperies {

    private int connectTimeout = 6000;
    private int socketTimeout = 15000;

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getSocketTimeout() {
        return socketTimeout;
    }

    public void setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    @Override
    public String toString() {
        return "HttpClientProperies{" +
                "connectTimeout=" + connectTimeout +
                ", socketTimeout=" + socketTimeout +
                '}';
    }
}
