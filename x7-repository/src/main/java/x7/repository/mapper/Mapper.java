/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package x7.repository.mapper;

import x7.core.bean.BeanElement;
import x7.core.bean.Parsed;
import x7.core.bean.Parser;
import x7.core.util.JsonX;
import x7.repository.DbType;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface Mapper {

    String CREATE = "CREATE";
    String REFRESH = "REFRESH";
    String REMOVE = "REMOVE";
    String QUERY = "QUERY";
    String LOAD = "LOAD";
    String TAG = "TAG";
    String CREATE_TABLE = "CREATE_TABLE";


    interface Interpreter {

        String getTableSql(Class clz);

        String getRefreshSql(Class clz);

        String getQuerySql(Class clz);

        String getLoadSql(Class clz);

        String getCreateSql(Class clz);

        String getTagSql(Class clz);
    }

    public static String getSqlTypeRegX(BeanElement be) {

        Class clz = be.clz;
        if (clz == Date.class || clz == java.sql.Date.class || clz == java.sql.Timestamp.class) {
            return Dialect.DATE;
        } else if (clz == String.class) {
            return Dialect.STRING;
        } else if (clz.isEnum()) {
            return Dialect.STRING;
        } else if (clz == int.class || clz == Integer.class) {
            return Dialect.INT;
        } else if (clz == long.class || clz == Long.class) {
            return Dialect.LONG;
        } else if (clz == double.class || clz == Double.class) {
            return Dialect.BIG;
        } else if (clz == float.class || clz == Float.class) {
            return Dialect.BIG;
        } else if (clz == BigDecimal.class) {
            return Dialect.BIG;
        } else if (clz == boolean.class || clz == Boolean.class) {
            return Dialect.BYTE;
        } else if (clz == short.class || clz == Short.class) {
            return Dialect.INT;
        } else if (clz == byte.class || clz == Byte.class) {
            return Dialect.BYTE;
        }
        return Dialect.TEXT;

    }

    interface Dialect {

        String DATE = " ${DATE}";
        String BYTE = " ${BYTE}";
        String INT = " ${INT}";
        String LONG = " ${LONG}";
        String BIG = " ${BIG}";
        String STRING = " ${STRING}";
        String TEXT = " ${TEXT}";
        String LONG_TEXT = " ${LONG_TEXT}";
        String INCREAMENT = " ${INCREAMENT}";
        String ENGINE = " ${ENGINE}";

        String match(String sql, long start, long rows);

        String match(String sql, String sqlType);


        <T> void initObj(T obj, ResultSet rs, BeanElement tempEle, List<BeanElement> eles);


        public static Object mappedResult(String property, String mapper, ResultSet rs) throws SQLException {

            Object obj = null;

            if (DbType.ORACLE.equals(DbType.value)) {

                if (property.contains(".")) {
                    mapper = mapper.substring(mapper.indexOf(".") + 1).toUpperCase();
                    obj = rs.getObject(mapper);
                }

            } else {
                obj = rs.getObject(mapper);
            }

            if (obj == null)
                return null;

            String[] arr = property.split("\\.");
            String clzName = arr[0];
            String p = arr[1];
            Parsed parsed = Parser.get(clzName);
            BeanElement element = parsed.getElement(p);

            Class ec = element.clz;

            if (obj instanceof BigDecimal && DbType.ORACLE.equals(DbType.value)) {

                BigDecimal bg = (BigDecimal) obj;
                if (ec == int.class || ec == Integer.class) {
                    return bg.intValue();
                } else if (ec == long.class || ec == Long.class) {
                    return bg.longValue();
                } else if (ec == double.class || ec == Double.class) {
                    return bg.doubleValue();
                } else if (ec == float.class || ec == Float.class) {
                    return bg.floatValue();
                }else if (ec == boolean.class || ec == Boolean.class) {
                    int i = bg.intValue();
                    return i == 0 ? false : true;
                } else if (ec == Date.class ) {
                    long l = bg.longValue();
                    return new Date(l);
                }  else if ( ec == java.sql.Date.class ) {
                    long l = bg.longValue();
                    return new java.sql.Date(l);
                } else if (ec == Timestamp.class) {
                    long l = bg.longValue();
                    return new Timestamp(l);
                } else if (ec == byte.class || ec == Byte.class) {
                    return bg.byteValue();
                }

            }else if (obj instanceof Timestamp && ec == Date.class){
                Timestamp ts = (Timestamp)obj;
                return new Date(ts.getTime());
            } if (ec.isEnum()) {
                return Enum.valueOf(ec, obj.toString());
            } else if (element.isJson){
               if (ec == List.class){
                   Class geneType = element.geneType;
                   return JsonX.toList(obj.toString(),geneType);
               }else if (ec == Map.class){
                   return JsonX.toMap(obj);
               }else{
                   return JsonX.toObject(obj.toString(),ec);
               }
            }

            return obj;
        }


        public static Object filterValue(Object value) {
            if (DbType.ORACLE.equals(DbType.value)) {

                if (value instanceof Date) {
                    Date date = (Date) value;
                    Timestamp timestamp = new Timestamp(date.getTime());
                    return timestamp;
                }else if (value instanceof Boolean){
                    Boolean b = (Boolean)value;
                    return b.booleanValue() == true ? 1 : 0;
                }
            }
            return value;
        }
    }
}
