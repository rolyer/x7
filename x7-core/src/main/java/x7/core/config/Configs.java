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
package x7.core.config;

import org.springframework.core.env.Environment;
import x7.core.util.KeyUtil;

import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class Configs {

	private static Map<String, Object> map = new ConcurrentHashMap<String, Object>();


	private static Environment environment;
	public static void setEnvironment(Environment env){
		environment = env;
	}


	public static Map<String, Object> referMap() {
		return map;
	}


	public static Object get(String keyStr) {

		String envObj = environment.getProperty(keyStr);
		if (Objects.nonNull(envObj))
			return envObj;

		
		List<String> keyList = KeyUtil.getKeyList(keyStr);

		Map<String, Object> tempMap = map;
		for (String key : keyList) {
			Object obj = tempMap.get(key);
			if (obj == null)
				return null;
			if (obj instanceof Map) {
				tempMap = (Map<String, Object>) obj;
			} else {
				return obj;
			}
		}
		return tempMap;
	}

	public static int getIntValue(String key) {

		String envObj = environment.getProperty(key);
		if (Objects.nonNull(envObj)) {
			return Integer.valueOf(envObj);
		}
		
		Integer value = 0;

		try {
			value = Integer.valueOf(get(key) + "");
		} catch (MissingResourceException mre) {
			String err = "请检查配置文件config/*.txt, 缺少key:" + key;
			System.err.println(err);
			mre.printStackTrace();
		} catch (Exception e) {
			String err = "请检查配置文件config/*.txt, 发现了:" + key + "=" + map.get(key);
			System.err.println(err);
			e.printStackTrace();
		}
		return value;
	}

	public static Map<String, Object> getMap(String key) {

		Object obj = get(key);
		if (obj instanceof Map)
			return (Map<String, Object>) obj;
		return null;
	}

	public static String getString(String key) {

		String envObj = environment.getProperty(key);
		if (Objects.nonNull(envObj))
			return envObj;

		String value = "";

		try {
			value = get(key) + "";
		} catch (MissingResourceException mre) {
			String err = "请检查配置文件config/*.txt, 缺少key:" + key;
			System.err.println(err);
			mre.printStackTrace();

		} catch (Exception e) {
			String err = "请检查配置文件config/*.txt, 发现了:" + key + "=" + map.get(key);
			System.err.println(err);
			e.printStackTrace();

		}

		return value;

	}

	public static long getLongValue(String key) {

		String envObj = environment.getProperty(key);
		if (Objects.nonNull(envObj)) {
			return Long.valueOf(envObj);
		}

		Long value = 0L;

		try {
			value = Long.valueOf(get(key) + "");
		} catch (MissingResourceException mre) {
			String err = "请检查配置文件config/*.txt, 缺少key:" + key;
			System.err.println(err);
			mre.printStackTrace();

		} catch (Exception e) {
			String err = "请检查配置文件config/*.txt, 发现了:" + key + "=" + map.get(key);
			System.err.println(err);
			e.printStackTrace();

		}
		return value;
	}

	public static boolean isTrue(String key) {

		String envObj = environment.getProperty(key);
		if (Objects.nonNull(envObj)) {
			return Boolean.parseBoolean(envObj);
		}

		String value = "";

		try {
			value = get(key) + "";
			return Boolean.parseBoolean(value);
		} catch (MissingResourceException mre) {
			String err = "请检查配置文件config/*.txt, 缺少key:" + key;
			System.err.println(err);
			mre.printStackTrace();

		} catch (Exception e) {
			String err = "请检查配置文件config/*.txt, 发现了:" + key + "=" + map.get(key);
			System.err.println(err);
			e.printStackTrace();

		}
		return false;
	}

}