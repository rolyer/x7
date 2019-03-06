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
package io.xream.x7.reyc.internal;

import io.xream.x7.reyc.LogBean;
import x7.core.util.ExceptionUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class HttpClientInvocationHandler implements InvocationHandler {

    private HttpClientProxy httpClientProxy;

    public HttpClientInvocationHandler(HttpClientProxy httpClientProxy){
        this.httpClientProxy = httpClientProxy;
    }
    
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        try{

            if (httpClientProxy.getBackend() == null)
                return ClientResolver.resolve(httpClientProxy.getObjectType().getName(),method.getName(),args);

            return ClientResolver.wrap(httpClientProxy,  new ClientResolver.BackendService() {
                @Override
                public Object decorate() {
                    return ClientResolver.resolve(httpClientProxy.getObjectType().getName(),method.getName(),args);
                }

                @Override
                public LogBean logBean() {
                    Object obj = (args == null || args.length == 0) ? null : args[0];
                    LogBean logBean = new LogBean();
                    logBean.setObj(obj);
                    logBean.setClz(obj == null ? null : obj.getClass());
                    ClientResolver.mapping(logBean,httpClientProxy.getObjectType().getName(),method.getName());
                    return logBean;
                }
            });

        } catch (RuntimeException re){
            throw re;
        } catch (Exception e){
            throw new RuntimeException(ExceptionUtil.getMessage(e));
        }
    }
}
