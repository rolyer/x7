package x7.repository.cache;

import java.util.List;
import java.util.Set;

public interface CacheStoragePolicy {
    boolean set(String key, String time);

    boolean delete(String key);

    Set<String> keys(String key);

    String get(String nsKey);

    boolean set(String key, String toJson, int validSecond);

    List<String> multiGet(List<String> keyArr);
}
