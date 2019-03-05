package io.xream.x7.reyc.internal;

import io.xream.x7.reyc.ReyClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ClientParser {


    private final static Map<String, ClientParsed> map = new HashMap<>();


    public static ClientParsed get(String intfName) {

        return map.get(intfName);
    }

    public static void parse(Class<?> clz) {

        Annotation reyClientAnno = clz.getAnnotation(ReyClient.class);
        if (reyClientAnno == null)
            return;

        ReyClient reyClient = (ReyClient) reyClientAnno;

        String url = reyClient.value();

        ClientParsed parsed = new ClientParsed();

        map.put(clz.getName(),parsed);

        parsed.setObjectType(clz);
        parsed.setUrl(url);

        Method[] arr = clz.getDeclaredMethods();

        for (Method method : arr) {

            String methodName = method.getName();
            Class<?> returnType = method.getReturnType();

            Annotation mappingAnno = method.getAnnotation(RequestMapping.class);
            if (mappingAnno == null)
                throw new RuntimeException(clz.getName()+"."+methodName+ ", Not Found Annotation: " + RequestMapping.class.getName());

            RequestMapping requestMapping = (RequestMapping) mappingAnno;
            if (requestMapping.value() == null || requestMapping.value().length ==0)
                throw new RuntimeException(clz.getName()+"."+methodName+ " RequestMapping, no mapping value");

            String mapping = requestMapping.value()[0];

            RequestMethod rm = RequestMethod.POST;

            RequestMethod[] rmArr = requestMapping.method();
            if (rmArr == null || rmArr.length == 0) {
                if (mapping.contains("{")&&mapping.contains("}")){
                    rm = RequestMethod.GET;
                }
            }else{
                rm = rmArr[0];
            }

            MethodParsed methodParsed = new MethodParsed();
            methodParsed.setRequestMapping(mapping);
            methodParsed.setReturnType(returnType);
            methodParsed.setRequestMethod(rm);

            parsed.getMap().put(methodName,methodParsed);
        }

    }

}
