package x7.repository;

import java.util.List;
import java.util.Map;

public interface Manuable {

     <T> boolean execute(T obj, String sql);

     List<Map<String,Object>> list(Class clz, String sql, List<Object> conditionList);
}
