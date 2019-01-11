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
package x7.core.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import x7.core.util.BeanUtil;
import x7.core.util.StringUtil;
import x7.core.web.Direction;
import x7.core.web.Paged;

import java.io.Serializable;
import java.util.*;

/**
 * 
 * @author sim
 *
 */
public class Criteria implements CriteriaCondition, Paged, Serializable {

	private static final long serialVersionUID = 7088698915888081349L;

	private Class<?> clz;
	@JsonIgnore
	private transient Parsed parsed;
	private boolean isScroll = true;
	private int page;
	private int rows;
	private List<String> orderByList = new ArrayList<>();
	private Direction direction = Direction.DESC;
	private boolean isFixedSort;

	private List<Object> valueList = new ArrayList<Object>();
	
	private List<X> listX = new ArrayList<X>();

	private transient DataPermission dataPermission;//String,Or List<String>   LikeRight | In

	public transient boolean isWhere = true;

	public Criteria(){}

	@Override
	public List<Object> getValueList() {
		return valueList;
	}

	public void setValueList(List<Object> valueList) {
		this.valueList = valueList;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction sc) {
		this.direction = sc;
	}

	public Class<?> getClz() {
		return clz;
	}

	public void setClz(Class<?> clz) {
		this.clz = clz;
	}

	public Parsed getParsed() {
		return parsed;
	}

	public void setParsed(Parsed parsed) {
		this.parsed = parsed;
	}

	public String sourceScript() {
		return BeanUtil.getByFirstLower(getClz().getSimpleName());
	}

	private transient String countDistinct = "COUNT(*) count";
	public void setCountDistinct(String str){
		this.countDistinct = str;
	}
	public String getCountDistinct(){
		return this.countDistinct;
	}

	private transient String customedResultKey = SqlScript.STAR;
	public void setCustomedResultKey(String str){
		this.customedResultKey = str;
	}

	public String resultAllScript() {
		return customedResultKey;
	}

	public String getOrderBy() {
		if (isFixedSort)
			return null;
		StringBuilder sb = new StringBuilder();
		for (String s : orderByList){
			sb.append(s);
		}
		return sb.toString();
	}

	public List<String> getOrderByList() {
		return orderByList;
	}

	public void setOrderByList(List<String> orderByList) {
		this.orderByList = orderByList;
	}

	public boolean isScroll() {
		return isScroll;
	}

	public void setScroll(boolean isScroll) {
		this.isScroll = isScroll;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	@Override
	public List<X> getListX() {
		return this.listX;
	}
	
	protected void add(X x) {
		this.listX.add(x);
	}

	public DataPermission getDataPermission() {
		return dataPermission;
	}

	public void setDataPermission(DataPermission dataPermission) {
		this.dataPermission = dataPermission;
	}

	public boolean isFixedSort() {
		return isFixedSort;
	}

	public void setFixedSort(boolean inConditionSort) {
		this.isFixedSort = inConditionSort;
	}

	public void paged(Paged paged) {
		String orderBy = paged.getOrderBy();
		if (StringUtil.isNotNull(orderBy)){
			String[] arr = orderBy.split(",");
			for (String s : arr){
				this.orderByList.add(s.trim());
			}
		}
		this.direction = paged.getDirection();
		this.isScroll = paged.isScroll();
		this.page = paged.getPage();
		this.rows = paged.getRows();
	}

	@Override
	public String toString() {
		return "Criteria{" +
				"isScroll=" + isScroll +
				", page=" + page +
				", rows=" + rows +
				", orderByList='" + orderByList + '\'' +
				", direction=" + direction +
				", valueList=" + valueList +
				", listX=" + listX +
				", dataPermission=" + dataPermission +
				", isWhere=" + isWhere +
				", countDistinct='" + countDistinct + '\'' +
				", customedResultKey='" + customedResultKey + '\'' +
				", clz=" + clz +
				'}';
	}

	public class ResultMapped extends Criteria {

		private List<String> resultKeyList = new ArrayList<String>();
		private String sourceScript;
		private Distinct distinct;
		private String groupBy;
		private List<Reduce> reduceList = new ArrayList<>();
		private MapMapper mapMapper;

		public Distinct getDistinct() {
			return distinct;
		}

		public List<Reduce> getReduceList() {
			return reduceList;
		}

		public String getGroupBy() {
			return groupBy;
		}

		public void setGroupBy(String groupBy) {
			this.groupBy = groupBy;
		}

		public void setReduceList(List<Reduce> reduceList) {
			this.reduceList = reduceList;
		}

		public void setDistinct(Distinct distinct) {
			this.distinct = distinct;
		}

		public MapMapper getMapMapper() {
			return this.mapMapper;
		}

		public void setMapMapper(MapMapper mapMapper) {
			this.mapMapper = mapMapper;
		}

		public String getResultScript() {
			if (resultKeyList.isEmpty()){
				return SqlScript.STAR;
			}else{
				StringBuilder sb = new StringBuilder();
				int i = 0;
				int size = resultKeyList.size() - 1;
				for (String str : resultKeyList){
					String mapper = getMapMapper().mapper(str);
					sb.append(mapper);
					if (i < size){
						sb.append(SqlScript.COMMA);
					}
					i++;
				}
				return sb.toString();
			}
		}

		public void setSourceScript(String sourceScript) {
			this.sourceScript = sourceScript;
		}
		

		public List<String> getResultKeyList() {
			return resultKeyList;
		}

		public void setResultKeyList(List<String> columnList) {
			this.resultKeyList = columnList;
		}


		@Override
		public String sourceScript() {
			if (sourceScript == null) {
				return BeanUtil.getByFirstLower(getClz().getSimpleName());
			} else {
				return sourceScript;
			}
		}


		@Override
		public String resultAllScript() {
			if (Objects.nonNull(super.customedResultKey)&&!super.customedResultKey.equals(SqlScript.STAR)){
				return super.customedResultKey;
			}else {
				int size = 0;
				String column = "";
				if (resultKeyList.isEmpty()) {
					column += (SqlScript.SPACE + SqlScript.STAR + SqlScript.SPACE);
				} else {
					size = resultKeyList.size();
					for (int i = 0; i < size; i++) {
						column = column + SqlScript.SPACE + resultKeyList.get(i);
						if (i < size - 1) {
							column += SqlScript.COMMA;
						}
					}
				}
				return column;
			}

		}

		public List<String> listAllResultKey() {
			List<String> list = new ArrayList<String>();
			Parsed parsed = Parser.get(getClz());

			for (BeanElement be : parsed.getBeanElementList()) {
				list.add(be.getMapper());
			}
			return list;
		}

		@Override
		public String toString() {
			return "ResultMapped{" +
					"resultKeyList=" + resultKeyList +
					", sourceScript='" + sourceScript + '\'' +
					", criteria='" + super.toString() + '\'' +
					'}';
		}

	}

	
	public static class X {
		private static final long serialVersionUID = 7088698915888083256L;
		private Conjunction conjunction;
		private Predicate predicate;
		private String key;
		private Object value;
		private List<X> subList;
		private X parent;
		private transient String script;
		public X(){}
		public Conjunction getConjunction() {
			return conjunction;
		}
		public void setConjunction(Conjunction conjunction) {
			this.conjunction = conjunction;
		}
		public Predicate getPredicate() {
			return predicate;
		}
		public void setPredicate(Predicate predicate) {
			this.predicate = predicate;
		}
		public String getKey() {
			return key;
		}
		public void setKey(String key) {
			this.key = key;
		}
		public Object getValue() {
			return value;
		}
		public void setValue(Object value) {
			this.value = value;
		}
		public List<X> getSubList() {
			return subList;
		}
		public void setSubList(List<X> subList) {
			this.subList = subList;
		}
		public X getParent() {
			return parent;
		}
		public void setParent(X parent) {
			this.parent = parent;
		}
		public String getScript() {
			return script;
		}
		public void setScript(String script) {
			this.script = script;
		}

		@Override
		public String toString() {
			return "X{" +
					"conjunction=" + conjunction +
					", predicate=" + predicate +
					", key=" + key +
					", value=" + value +
					", subList=" + subList +
					", script=" + script +
					'}';
		}
	}

	public enum ReduceType {
		SUM,
		COUNT,
		MAX,
		MIN,
		AVG
	}

	public static class MapMapper {
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

}