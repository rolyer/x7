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

import com.alibaba.fastjson.JSON;
import x7.core.util.JsonX;

import java.util.Objects;

public class GenericObject<T> {

	private Class<T> clzz;
	private T obj;

	public GenericObject(){}
	public GenericObject(T t){
		this.obj = t;
	}

	public Class<T> getClzz() {
		if (this.clzz == null) {
			this.clzz = (Class<T>) obj.getClass();
		}
		return this.clzz;
	}
	public void setClzz(Class<T> clz) {
		this.clzz = clz;
	}

	public T getObj() {
		if (Objects.nonNull(obj) ){
			if (this.obj instanceof JSON) {
				this.obj = JsonX.toObject(obj, clzz);
			}
		}
		return obj;
	}
	public void setObj(T obj) {
		this.obj = obj;
	}
	@Override
	public String toString() {
		return ""+getObj();
	}
	
}
