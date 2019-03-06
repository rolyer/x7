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

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMethod;
import x7.core.exception.RemoteServiceException;
import x7.core.util.HttpClientUtil;
import x7.core.util.JsonX;
import x7.core.util.StringUtil;

import java.util.List;
import java.util.function.Supplier;
import java.util.regex.Pattern;

public class ClientResolver {


    private static Logger logger = LoggerFactory.getLogger(ClientResolver.class);

    private static CircuitBreakerRegistry circuitBreakerRegistry;

    private static HttpClientProperies properies;


    public static void init(CircuitBreakerRegistry c,HttpClientProperies p) {
        circuitBreakerRegistry = c;
        properies = p;
    }

    private static Pattern pattern = Pattern.compile("\\{[\\w]*\\}");

    protected static Object resolve(String remoteIntfName, String methodName, Object[] args) {

        ClientParsed parsed = ClientParser.get(remoteIntfName);
        String url = parsed.getUrl();
        if (!url.startsWith("http")) {
            url = "http://" + url;
        }
        MethodParsed methodParsed = parsed.getMap().get(methodName);

        if (methodParsed == null)
            throw new RuntimeException("RequestMapping NONE: " + remoteIntfName + "." + methodName);

        String mapping = methodParsed.getRequestMapping();
        RequestMethod requestMethod = methodParsed.getRequestMethod();

        url = url + mapping;

        String result = null;

        if (requestMethod == RequestMethod.POST) {

            if (args != null && args.length > 0) {
                result = HttpClientUtil.post(url, args[0],methodParsed.getHeaderList(),properies.getConnectTimeout(),properies.getSocketTimeout());
            } else {
                result = HttpClientUtil.post(url, null,methodParsed.getHeaderList(), properies.getConnectTimeout(),properies.getSocketTimeout());
            }
        } else {
            List<String> regExList = StringUtil.listByRegEx(url, pattern);
            int size = regExList.size();
            for (int i = 0; i < size; i++) {
                url = url.replaceAll(regExList.get(i), args[i].toString());
            }
            result = HttpClientUtil.getUrl(url,methodParsed.getHeaderList(),properies.getConnectTimeout(),properies.getSocketTimeout());
        }

        if (StringUtil.isNullOrEmpty(result))
            return null;

        hanleException(result);

        Class<?> returnType = methodParsed.getReturnType();
        if (returnType == null || returnType == void.class) {
            return null;
        }

        Object obj = JsonX.toObject(result, returnType);

        return obj;
    }


    public interface BackendService {
        Object decorate();
    }

    protected static Object wrap(String backend, BackendService backendService) {

        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker(backend);

        Supplier<Object> circuitBreakerSupplier = CircuitBreaker
                .decorateSupplier(circuitBreaker, backendService::decorate);


        Object result = Try.ofSupplier(circuitBreakerSupplier)
                .recover(e ->
                        hanleException(e)
                ).get();

        System.out.println(result);
        return result;
    }

    /**
     * @param e
     * @return
     */
    private static Object hanleException(Throwable e) {

        if (e.toString().contains("HttpHostConnectException")) {

            if (logger.isErrorEnabled()){
                logger.error(e.getMessage());
            }

            throw new RuntimeException(e.getMessage());
        }

        return e.toString();
    }

    private static void hanleException(String result) {

        if (result.contains("RemoteServiceException")
                || result.contains("RuntimeException")
                || result.contains("BizException")
                || result.contains("\"status\":\"FAIL\"")) {

            if (logger.isErrorEnabled()){
                logger.error(result);
            }

            throw new RemoteServiceException(result);
        }

    }

}
