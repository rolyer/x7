package x7.core.bean;

import java.util.HashMap;
import java.util.Map;

public class MapMapper {
    private Map<String, String> propertyMapperMap = new HashMap<String, String>();
    private Map<String, String> mapperPropertyMap = new HashMap<String, String>();

    public Map<String, String> getPropertyMapperMap() {
        return propertyMapperMap;
    }

    public Map<String, String> getMapperPropertyMap() {
        return mapperPropertyMap;
    }

    public void put(String property, String mapper) {
        this.propertyMapperMap.put(property, mapper);
        this.mapperPropertyMap.put(mapper, property);
    }

    public String mapper(String property) {
        return this.propertyMapperMap.get(property);
    }

    public String property(String mapper) {
        return this.mapperPropertyMap.get(mapper);
    }

    @Override
    public String toString() {
        return "MapMapper [propertyMapperMap=" + propertyMapperMap + ", mapperPropertyMap=" + mapperPropertyMap
                + "]";
    }
}