package x7.repository.mapper;

import x7.core.bean.*;
import x7.core.util.JsonX;
import x7.core.util.StringUtil;
import x7.repository.DbType;
import x7.repository.exception.SqlTypeException;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

public class OracleDialect implements Mapper.Dialect {

    private Map<String, String> map = new HashMap<String, String>() {
        {
            put(DATE, "date");
            put(BYTE, "number(3, 0)");
            put(INT, "number(10, 0)");
            put(LONG, "number(18, 0)");
            put(BIG, "number(19, 2)");
            put(STRING, "varchar2");
            put(TEXT, "clob");
            put(LONG_TEXT, "clob");
            put(INCREAMENT, "");
            put(ENGINE, "");
        }

    };

    private final static String ORACLE_PAGINATION = "SELECT * FROM (SELECT A.*, ROWNUM RN FROM ( ${SQL} ) A   WHERE ROWNUM <= ${END}  )  WHERE RN > ${BEGIN} ";
    private final static String ORACLE_PAGINATION_REGX_SQL = "${SQL}";
    private final static String ORACLE_PAGINATION_REGX_BEGIN = "${BEGIN}";
    private final static String ORACLE_PAGINATION_REGX_END = "${END}";

    public String match(String sql, long start, long rows) {

        return ORACLE_PAGINATION.replace(ORACLE_PAGINATION_REGX_END, String.valueOf(start + rows))
                .replace(ORACLE_PAGINATION_REGX_BEGIN, String.valueOf(start)).replace(ORACLE_PAGINATION_REGX_SQL, sql);

    }

    public String match(String sql, String sqlType) {
        String dateV = map.get(DATE);
        String byteV = map.get(BYTE);
        String intV = map.get(INT);
        String longV = map.get(LONG);
        String bigV = map.get(BIG);
        String textV = map.get(TEXT);
        String longTextV = map.get(LONG_TEXT);
        String stringV = map.get(STRING);
        String increamentV = map.get(INCREAMENT);
        String engineV = map.get(ENGINE);

        return sql.replace(DATE.trim(), dateV).replace(BYTE.trim(), byteV).replace(INT.trim(), intV)
                .replace(LONG.trim(), longV).replace(BIG.trim(), bigV).replace(TEXT.trim(), textV)
                .replace(LONG_TEXT.trim(), longTextV).replace(STRING.trim(), stringV)
                .replace(INCREAMENT.trim(), increamentV).replace(ENGINE.trim(), engineV);
    }

    private Object getObject(final String mapper, ResultSet rs, BeanElement element) throws SQLException, IOException {

        Object obj = null;
        Class ec = element.clz;

        if (element.isJson) {
            java.sql.Clob clob = rs.getClob(mapper);
            Reader reader = clob.getCharacterStream();
            char[] charArr = new char[(int) clob.length()];
            reader.read(charArr);
            reader.close();

            String str = new String(charArr);//FIXME UIF-8 ?
            if (StringUtil.isNotNull(str)) {
                if (!(str.startsWith("{") || str.startsWith("[")))
                    return str;
                if (ec == List.class) {
                    Class geneType = element.geneType;
                    return JsonX.toList(obj.toString(), geneType);
                } else if (ec == Map.class) {
                    return JsonX.toMap(obj);
                } else {
                    return JsonX.toObject(obj.toString(), ec);
                }
            }
        }

        obj = rs.getObject(mapper);

        if (obj == null)
            return null;

        if (obj instanceof BigDecimal) {

            BigDecimal bg = (BigDecimal) obj;
            if (ec == BigDecimal.class) {
                return bg;
            } else if (ec == int.class || ec == Integer.class) {
                return bg.intValue();
            } else if (ec == long.class || ec == Long.class) {
                return bg.longValue();
            } else if (ec == double.class || ec == Double.class) {
                return bg.doubleValue();
            } else if (ec == float.class || ec == Float.class) {
                return bg.floatValue();
            } else if (ec == boolean.class || ec == Boolean.class) {
                int i = bg.intValue();
                return i == 0 ? false : true;
            } else if (ec == Date.class) {
                long l = bg.longValue();
                return new Date(l);
            } else if (ec == java.sql.Date.class) {
                long l = bg.longValue();
                return new java.sql.Date(l);
            } else if (ec == Timestamp.class) {
                long l = bg.longValue();
                return new Timestamp(l);
            } else if (ec == byte.class || ec == Byte.class) {
                return bg.byteValue();
            }

        } else if (obj instanceof Timestamp && ec == Date.class) {
            Timestamp ts = (Timestamp) obj;
            return new Date(ts.getTime());
        }
        if (ec.isEnum()) {
            return Enum.valueOf(ec, obj.toString());
        }

        return obj;

    }

    @Override
    public Object mappedResult(String property, String mapper, ResultSet rs) throws SQLException, IOException {

        String[] arr = property.split("\\.");
        String clzName = arr[0];
        String p = arr[1];
        Parsed parsed = Parser.get(clzName);
        BeanElement element = parsed.getElement(p);

        if (mapper.contains("`")) {
            mapper = mapper.replace("`", "");
        }

        if (mapper.contains(".")) {
            mapper = mapper.replace(".", "#");
        }


        return getObject(mapper, rs, element);
    }

    @Override
    public <T> void initObj(T obj, ResultSet rs, BeanElement tempEle, List<BeanElement> eles) throws IOException, SQLException, InvocationTargetException, IllegalAccessException {


        for (BeanElement ele : eles) {

            Method method = ele.setMethod;
            String mapper = ele.getMapper();


            Object value = getObject(mapper, rs, ele);
            if (value != null) {
                method.invoke(obj, value);
            }

        }
    }

    @Override
    public String resultScript(String sql) {

        String temp = sql.replaceFirst(SqlScript.SELECT, "");
        String[] arr = temp.split(SqlScript.FROM);
        String left = arr[0];
        String right = arr[1];

        String[] keyArr = left.split(",");
        for (String origin : keyArr) {
            origin = origin.trim();
            String target = origin + " AS " + origin.replace(".", "#");
            left = left.replace(origin, target);
        }

        StringBuffer sb = new StringBuffer();
        sb.append(SqlScript.SELECT).append(SqlScript.SPACE);
        sb.append(left);
        sb.append(SqlScript.SPACE).append(SqlScript.FROM).append(SqlScript.SPACE);
        sb.append(right);

        return sb.toString();


    }

    public  void setJSON(int i, String str, PreparedStatement pstmt) throws SQLException, IOException {

        Reader reader = new StringReader(str);
        pstmt.setNClob(i, reader);
        reader.close();//FIXME ?

    }

    public void setObject(int i, Object obj, PreparedStatement pstm) throws SQLException {
        if (obj instanceof Reader){
            Reader reader = (Reader)obj;
            pstm.setNClob(i, reader);
        }else{
            pstm.setObject(i, obj);
        }
    }

}
