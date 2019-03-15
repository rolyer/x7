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
import io.github.resilience4j.circuitbreaker.CircuitBreakerOpenException;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryRegistry;
import io.vavr.control.Try;
import io.xream.x7.reyc.Url;
import io.xream.x7.reyc.ReyClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMethod;
import x7.core.exception.BusyException;
import x7.core.exception.RemoteServiceException;
import x7.core.util.HttpClientUtil;
import x7.core.util.JsonX;
import x7.core.util.StringUtil;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.regex.Pattern;

public class ClientResolver {


    private static Logger logger = LoggerFactory.getLogger(ReyClient.class);

    private static CircuitBreakerRegistry circuitBreakerRegistry;
    private static RetryRegistry retryRegistry;

    private static HttpClientProperies properies;


    public static void init(HttpClientProperies p, CircuitBreakerRegistry c, RetryRegistry r) {
        circuitBreakerRegistry = c;
        properies = p;
        retryRegistry = r;
    }

    private static Pattern pattern = Pattern.compile("\\{[\\w]*\\}");


    protected static Object resolve(String remoteIntfName, String methodName, Object[] args) {

        ClientParsed parsed = ClientParser.get(remoteIntfName);
        String url = parsed.getUrl();
        MethodParsed methodParsed = parsed.getMap().get(methodName);

        if (methodParsed == null)
            throw new RuntimeException("RequestMapping NONE: " + remoteIntfName + "." + methodName);

        String mapping = methodParsed.getRequestMapping();

        url = url + mapping;

        List<Object> objectList = new ArrayList<>();
        boolean flag = false;
        for (Object arg : args){
            if (arg != null && arg instanceof Url){
                Url dynamicUrl = (Url)arg;
                url = dynamicUrl.value();
                flag = true;
            }else{
                objectList.add(arg);
            }
        }
        if (flag) {
            args = objectList.toArray();
        }

        if (!url.startsWith("http")) {
            url = "http://" + url;
        }

        String result = null;

        RequestMethod requestMethod = methodParsed.getRequestMethod();

        if (requestMethod == RequestMethod.POST) {

            if (args != null && args.length > 0) {
                result = HttpClientUtil.post(url, args[0], methodParsed.getHeaderList(), properies.getConnectTimeout(), properies.getSocketTimeout());
            } else {
                result = HttpClientUtil.post(url, null, methodParsed.getHeaderList(), properies.getConnectTimeout(), properies.getSocketTimeout());
            }
        } else {
            List<String> regExList = StringUtil.listByRegEx(url, pattern);
            int size = regExList.size();
            for (int i = 0; i < size; i++) {
                url = url.replaceAll(regExList.get(i), args[i].toString());
            }
            result = HttpClientUtil.getUrl(url, methodParsed.getHeaderList(), properies.getConnectTimeout(), properies.getSocketTimeout());
        }

        if (StringUtil.isNullOrEmpty(result))
            return null;

        hanleRemoteException(result);

        Class<?> returnType = methodParsed.getReturnType();
        if (returnType == null || returnType == void.class) {
            return null;
        }

        Object obj = JsonX.toObject(result, returnType);

        return obj;
    }

    protected static Object wrap(HttpClientProxy proxy, String methodName, BackendService backendService) {

        String backend = proxy.getBackend();
        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker(backend);

        Supplier<Object> decoratedSupplier = CircuitBreaker
                .decorateSupplier(circuitBreaker, backendService::decorate);

        final String intfName = proxy.getObjectType().getName();
        final String tag = intfName + "." + methodName;

        if (proxy.isRetry()) {
            Retry retry = retryRegistry.retry(backend);
            if (retry != null) {

                retry.getEventPublisher()
                        .onRetry(event -> {
                            if (logger.isDebugEnabled()) {
                                logger.debug(event.getEventType().toString() + "_" + event.getNumberOfRetryAttempts() + ": "
                                        + tag);
                            }
                        });

                decoratedSupplier = Retry
                        .decorateSupplier(retry, decoratedSupplier);
            }
        }

        Object result = Try.ofSupplier(decoratedSupplier)
                .recover(e ->
                        hanleException(e, tag, backendService)
                ).get();

        return result;
    }

    /**
     * @param e
     * @return
     */
    private static Object hanleException(Throwable e, String tag, BackendService backendService) {

        if (e instanceof RemoteServiceException) {
            throw (RemoteServiceException) e;
        }
        if (e instanceof CircuitBreakerOpenException) {
            backendService.fallback();
            if (logger.isErrorEnabled()) {
                logger.error(tag + ": " + e.getMessage());
            }
            throw new BusyException();
        }

        String str = e.toString();
        if (str.contains("HttpHostConnectException")
                || str.contains("ConnectTimeoutException")
                || str.contains("ConnectException")
        ) {
            backendService.fallback();
            if (logger.isErrorEnabled()) {
                logger.error(tag + ": " + e.getMessage());
            }
            throw new RuntimeException(tag + ": " + e.getMessage());
        }

        if (e instanceof RuntimeException) {
            if (logger.isErrorEnabled()) {
                logger.error(tag + ": " + e.getMessage());
            }
            throw new RuntimeException(tag + ": " + e.getMessage());
        }

        throw new RuntimeException(tag + ": " + e.getMessage());
    }

    private static void hanleRemoteException(String result) {

        if (result.contains("RemoteServiceException")
                || result.contains("RuntimeException")
                || result.contains("BizException")
                || result.contains("\"status\":\"FAIL\"")) {

            if (logger.isErrorEnabled()) {
                logger.error(result);
            }

            throw new RemoteServiceException(result);
        }

    }

    public static Object fallback(String intfName, String methodName, Object[] args) {

        ClientParsed parsed = ClientParser.get(intfName);
        if (parsed.getFallback() == null)
            return null;
        Method method = parsed.getFallbackMethodMap().get(methodName);

        if (method == null)
            return null;

        try {
            return method.invoke(parsed.getFallback(), args);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Exception of fallback");
        }

    }

    public interface BackendService {
        Object decorate();

        Object fallback();
    }

}
