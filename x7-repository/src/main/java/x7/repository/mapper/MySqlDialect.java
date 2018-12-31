package x7.repository.mapper;

import x7.core.bean.BeanElement;
import x7.core.bean.Parsed;
import x7.core.bean.Parser;
import x7.core.bean.SqlScript;
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

public class MySqlDialect implements Mapper.Dialect {

	private Map<String, String> map = new HashMap<String, String>() {
		{

			put(DATE, "timestamp");
			put(BYTE, "tinyint(1)");
			put(INT, "int(11)");
			put(LONG, "bigint(13)");
			put(BIG, "decimal(15,2)");
			put(STRING, "varchar");
			put(TEXT, "text");
			put(LONG_TEXT, "longtext");
			put(INCREAMENT, "AUTO_INCREMENT");
			put(ENGINE, "ENGINE=InnoDB DEFAULT CHARSET=utf8");

		}

	};

	public String match(String sql, long start, long rows) {

		StringBuilder sb = new StringBuilder();
		sb.append(sql).append(SqlScript.LIMIT).append(start).append(",").append(rows);
		return sb.toString();

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


	private  Object getObject(final String mapper, ResultSet rs, BeanElement element) throws SQLException, IOException {

		Class ec = element.clz;
		Object obj = rs.getObject(mapper);

		if (obj == null)
			return null;

		if (ec.isEnum()) {
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
		}else if (ec == BigDecimal.class) {
			return new BigDecimal(String.valueOf(obj));
		}else if (ec == double.class || ec == Double.class){
			return Double.valueOf(obj.toString());
		}

		return obj;
	}

	@Override
	public  Object mappedResult(String property, String mapper, ResultSet rs) throws SQLException, IOException {

		String[] arr = property.split("\\.");
		String clzName = arr[0];
		String p = arr[1];
		Parsed parsed = Parser.get(clzName);
		BeanElement element = parsed.getElement(p);

		if (mapper.contains("`")){
			mapper = mapper.replace("`","");
		}

		return getObject(mapper, rs, element);

	}

	@Override
	public <T> void initObj(T obj, ResultSet rs, BeanElement tempEle, List<BeanElement> eles) throws IOException, SQLException, InvocationTargetException, IllegalAccessException {

			for (BeanElement ele : eles) {

				Method method = ele.setMethod;
				String mapper = ele.getMapper();

				if (mapper.contains("`")){
					mapper = mapper.replace("`","");
				}

				Object value = getObject(mapper, rs, ele);
				method.invoke(obj, value);

			}


	}

	@Override
	public  String resultScript(String sql){
		return sql;
	}

	public void setJSON(int i, String str, PreparedStatement pstmt) throws SQLException, IOException {

		pstmt.setString(i, str);

	}

	public void setObject(int i, Object obj, PreparedStatement pstm) throws SQLException {

		pstm.setObject(i, obj);

	}
}
