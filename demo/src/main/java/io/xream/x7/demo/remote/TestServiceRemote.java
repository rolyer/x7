package io.xream.x7.demo.remote;


import io.xream.x7.demo.CatRO;
import io.xream.x7.reyc.DynamicUrl;
import io.xream.x7.reyc.ReyClient;
import org.springframework.web.bind.annotation.RequestMapping;


@ReyClient(value = "127.0.0.1:8868", circuitBreaker = "", retry = true, fallback = TestFallback.class)
public interface TestServiceRemote {


    @RequestMapping(value = "/xxx/reyc/test")
    Boolean test(CatRO ro, DynamicUrl dynamicUrl);

    @RequestMapping(value = "/xxx/reyc/test")
    Boolean testFallBack(CatRO ro);

}
