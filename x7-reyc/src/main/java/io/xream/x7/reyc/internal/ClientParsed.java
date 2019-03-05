package io.xream.x7.reyc.internal;

import java.util.HashMap;
import java.util.Map;

public class ClientParsed {

    private Class<?> objectType;
    private String url;
    private Map<String,MethodParsed> map = new HashMap<>();

    public Class<?> getObjectType() {
        return objectType;
    }

    public void setObjectType(Class<?> objectType) {
        this.objectType = objectType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, MethodParsed> getMap() {
        return map;
    }

    public void setMap(Map<String, MethodParsed> map) {
        this.map = map;
    }

    @Override
    public String toString() {
        return "ClientParsed{" +
                "objectType=" + objectType +
                ", url='" + url + '\'' +
                ", map=" + map +
                '}';
    }
}
