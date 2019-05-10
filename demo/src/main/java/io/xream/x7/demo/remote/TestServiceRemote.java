package io.xream.x7.demo.remote;


import io.xream.x7.demo.CatRO;
import io.xream.x7.reyc.Url;
import io.xream.x7.reyc.ReyClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@ReyClient(value = "127.0.0.1:8868", circuitBreaker = "", retry = true, fallback = TestFallback.class)
public interface TestServiceRemote {


    @RequestMapping(value = "/xxx/reyc/test")
    Boolean test(CatRO ro, Url url);

    @RequestMapping(value = "/xxx/reyc/test")
    Boolean testFallBack(CatRO ro);

    @RequestMapping(value = "/xxx/time/test", method = RequestMethod.GET)
    Boolean testTimeJack();

    @RequestMapping(value = "/xxx/reyc/base", method = RequestMethod.GET)
    int getBase();

}
