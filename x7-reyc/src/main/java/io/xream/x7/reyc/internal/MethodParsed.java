package io.xream.x7.reyc.internal;

import org.springframework.web.bind.annotation.RequestMethod;

public class MethodParsed {

    private String requestMapping;
    private RequestMethod requestMethod;
    private Class<?> returnType;

    public String getRequestMapping() {
        return requestMapping;
    }

    public void setRequestMapping(String requestMapping) {
        this.requestMapping = requestMapping;
    }

    public RequestMethod getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(RequestMethod requestMethod) {
        this.requestMethod = requestMethod;
    }

    public Class<?> getReturnType() {
        return returnType;
    }

    public void setReturnType(Class<?> returnType) {
        this.returnType = returnType;
    }

    @Override
    public String toString() {
        return "MethodParsed{" +
                "requestMapping='" + requestMapping + '\'' +
                ", requestMethod=" + requestMethod +
                ", returnType=" + returnType +
                '}';
    }
}
