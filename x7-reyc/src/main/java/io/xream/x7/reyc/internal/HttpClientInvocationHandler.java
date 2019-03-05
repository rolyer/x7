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

            if (httpClientProxy.getBackend() == null)
                return ClientResolver.resolve(httpClientProxy.getObjectType().getName(),method.getName(),args);

            return ClientResolver.wrap(httpClientProxy.getBackend(), new ClientResolver.BackendService() {
                @Override
                public Object decorate() {
                    return ClientResolver.resolve(httpClientProxy.getObjectType().getName(),method.getName(),args);
                }
            });

        } catch (Exception e){
            throw new RuntimeException(ExceptionUtil.getMessage(e));
        }
    }
}
