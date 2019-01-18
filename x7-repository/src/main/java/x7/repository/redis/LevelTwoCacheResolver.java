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
package x7.repository.redis;

import org.apache.log4j.Logger;
import x7.core.repository.CacheException;
import x7.core.repository.CacheResolver;
import x7.core.util.JsonX;
import x7.core.util.StringUtil;
import x7.core.util.VerifyUtil;
import x7.core.web.Page;
import x7.repository.exception.PersistenceException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * 
 * Level Two Cache
 * @author sim
 *
 */
public class LevelTwoCacheResolver implements CacheResolver {

	private final static Logger logger = Logger.getLogger(LevelTwoCacheResolver.class);
	public final static String NANO_SECOND = ".N_S";
	
	private static LevelTwoCacheResolver instance = null;
	public static LevelTwoCacheResolver getInstance(){
		if (instance == null){
			instance = new LevelTwoCacheResolver();
		}
		return instance;
	}

	private int validSecond;
	public void setValidSecond(int validSecond){
		this.validSecond = validSecond;
		System.out.println("\n");
		logger.info("L2 Cache started, cache time = " + validSecond + "s");
		System.out.println("\n");
	}
	private int getValidSecondAdjusted(){
		return  this.validSecond;
	}
	
	/**
	 * 标记缓存要更新
	 * @param clz
	 * @return nanuTime_String
	 */
	@SuppressWarnings("rawtypes")
	public String markForRefresh(Class clz){
		String key = getNSKey(clz);
		String time = String.valueOf(System.nanoTime());
		boolean flag = JedisConnector_Cache.getInstance().set(key, time);
		if (!flag)
			throw new CacheException("markForRefresh failed");
		return time;
	}
	
	/**
	 * 
	 * FIXME {hash tag}
	 */
	@SuppressWarnings("rawtypes")
	public void remove(Class clz, String key){
		key = getSimpleKey(clz, key);
		boolean flag = JedisConnector_Cache.getInstance().delete(key);
		if (!flag)
			throw new CacheException("remove failed");
	}

	public void remove(Class clz) {

		String key = getSimpleKey(clz);

		Set<String> keySet = JedisConnector_Cache.getInstance().keys(key);

		for (String k : keySet) {
			boolean flag = JedisConnector_Cache.getInstance().delete(k);
			if (!flag)
				throw new CacheException("remove failed");
		}

	}
	
	@SuppressWarnings("rawtypes")
	private String getNSKey(Class clz){
		return clz.getName()+ NANO_SECOND;
	}
	
	@SuppressWarnings("unused")
	private String getNS(String nsKey){
		return JedisConnector_Cache.getInstance().get(nsKey);
	}
	
	@SuppressWarnings("rawtypes")
	private List<String> getKeyList(Class clz, List<String> conditionList){
		if (conditionList == null || conditionList.isEmpty())
			return null;
		List<String> keyList = new ArrayList<>();
		for (String condition : conditionList){
			String key = getSimpleKey(clz, condition);
			keyList.add(key);
		}
		if (keyList.isEmpty())
			return null;

		return keyList;
	}

//	@SuppressWarnings("rawtypes")
//	private List<byte[]> getKeyList(Class clz, List<String> conditionList){
//		if (conditionList == null || conditionList.isEmpty())
//			return null;
//		List<byte[]> keyList = new ArrayList<byte[]>();
//		for (String condition : conditionList){
//			String key = getSimpleKey(clz, condition);
//			keyList.add(key.getBytes());
//		}
//		if (keyList.isEmpty())
//			return null;
//		List<byte[]> arrList= new ArrayList<byte[]>();
//
//		int i = 0;
//		for (byte[] keyB : keyList){
//			arrList.add(keyB);
//		}
//		return arrList;
//	}
	
	/**
	 * FIXME 有简单simpleKey的地方全改成字符串存储, value为bytes, new String(bytes)
	 * @param clz
	 * @param condition
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private String getSimpleKey(Class clz, String condition){
		return "{"+clz.getName()+"}." + condition;
	}

	private String getSimpleKey(Class clz){
		return "{"+clz.getName()+"}.*" ;
	}
	
	
	@SuppressWarnings("rawtypes")
	private String getKey(Class clz, Object conditionObj){
		String condition = JsonX.toJson(conditionObj);
		long startTime = System.currentTimeMillis();
		String key =  VerifyUtil.toMD5(getPrefix(clz) + condition);
		long endTime = System.currentTimeMillis();
		System.out.println("time_getKey = "+(endTime - startTime));
		return key;
	}

	
	/**
	 * 获取缓存KEY前缀
	 * @param clz
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private String getPrefix(Class clz){
		String key = getNSKey(clz);
		String nsStr = JedisConnector_Cache.getInstance().get(key);
		if (nsStr == null){
			String str = markForRefresh(clz);
			return clz.getName() + str;
		}
		return clz.getName() + nsStr;
	}

	/**
	 * FIXME {hash tag}
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void set(Class clz, String key, Object obj) {
		key = getSimpleKey(clz, key);
		int validSecond =  getValidSecondAdjusted();
		JedisConnector_Cache.getInstance().set(key, JsonX.toJson(obj), validSecond);
	}


	@SuppressWarnings("rawtypes")
	@Override
	public void setResultKeyList(Class clz, Object condition, List<String> keyList) {
		String key = getKey(clz, condition);
		try{
			JedisConnector_Cache.getInstance().set(key, JsonX.toJson(keyList), validSecond);
		}catch (Exception e) {
			throw new PersistenceException(e.getMessage());
		}
	}

	
	@Override
	public <T> void setResultKeyListPaginated(Class<T> clz, Object condition, Page<T> pagination) {
		
		String key = getKey(clz, condition);
		try{
			JedisConnector_Cache.getInstance().set(key, JsonX.toJson(pagination), validSecond);
		}catch (Exception e) {
			throw new PersistenceException(e.getMessage());
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<String> getResultKeyList(Class clz, Object condition) {
		String key = getKey(clz, condition);
		System.out.println("get key: " + key);
		long startTime = System.currentTimeMillis();
		String str = JedisConnector_Cache.getInstance().get(key);
		long endTime = System.currentTimeMillis();
		System.out.println("time_getResultKeyList = "+(endTime - startTime));
		if (StringUtil.isNullOrEmpty(str))
			return new ArrayList<String>();
		
		return JsonX.toList(str, String.class);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Page<String> getResultKeyListPaginated(Class clz, Object condition) {
		String key = getKey(clz, condition);
		System.out.println("get key: " + key);
		String json = JedisConnector_Cache.getInstance().get(key);
		
		if (StringUtil.isNullOrEmpty(json))
			return null;
		
		return ObjectUtil.toPagination(json, String.class);
	}

	@Override
	public <T> List<T> list(Class<T> clz, List<String> keyList) {
		List<String> keyArr = getKeyList(clz, keyList);//转换成缓存需要的keyList
		
		List<String> jsonList = JedisConnector_Cache.getInstance().mget(keyArr);
		
		if (jsonList == null)
			return new ArrayList<T>();
		
		List<T> list = new ArrayList<T>();
		for (String json : jsonList){
			if (StringUtil.isNotNull(json)) {
				T t = JsonX.toObject(json,clz);
				list.add(t);
			}
		}
		
		return list;
	}

	/**
	 * FIXME {hash tag}
	 */
	@Override
	public <T> T get(Class<T> clz, String key) {
		key = getSimpleKey(clz,key);
		String str = JedisConnector_Cache.getInstance().get(key);
		if (StringUtil.isNullOrEmpty(str))
			return null;
		T obj = JsonX.toObject(str,clz);
		return obj;
	}

	@Override
	public void setMapList(Class clz, String key, List<Map<String, Object>> mapList) {
		key = getSimpleKey(clz, key);
		int validSecond =  getValidSecondAdjusted();
		
		JedisConnector_Cache.getInstance().set(key, JsonX.toJson(mapList), validSecond);
	}

	@Override
	public List<Map<String, Object>> getMapList(Class clz, String key) {
		
		key = getSimpleKey(clz,key);
		String str = JedisConnector_Cache.getInstance().get(key);
		if (StringUtil.isNullOrEmpty(str))
			return null;
		List mapList = (List)JsonX.toList(str,Map.class);
		return mapList;
	}

}
