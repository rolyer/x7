package io.xream.x7.reyc.internal;

import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.Proxy;

public class HttpClientProxy implements FactoryBean {

    private Class<?> objectType;

    private String backend;


    @Override
    public Object getObject() throws Exception {
        return Proxy.newProxyInstance(objectType.getClassLoader(), new Class[]{objectType},new HttpClientInvocationHandler(this));
    }


    public void setObjectType(Class<?> objectType){
        this.objectType = objectType;
    }

    @Override
    public Class<?> getObjectType() {
        return this.objectType;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public String getBackend() {
        return backend;
    }

    public void setBackend(String backend) {
        this.backend = backend;
    }

}
