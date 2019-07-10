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

            final String methodName = method.getName();

            R r = ClientResolver.r(httpClientProxy.getObjectType().getName(),methodName,args);

            if (httpClientProxy.getBackend() == null) {
                String result = ClientResolver.resolve(r);
                return ClientResolver.toObject(r.getReturnType(),r.getGeneType(),result);
            }

            String result = ClientResolver.wrap(httpClientProxy, methodName, new ClientResolver.BackendService() {
                @Override
                public String decorate() {
                    return ClientResolver.resolve(r);
                }

                @Override
                public Object fallback() {
                    return ClientResolver.fallback(httpClientProxy.getObjectType().getName(),methodName,args);
                }
            });

            return ClientResolver.toObject(r.getReturnType(),r.getGeneType(),result);

        } catch (RuntimeException re){
            throw re;
        } catch (Exception e){
            throw new RuntimeException(ExceptionUtil.getMessage(e));
        }
    }
}
