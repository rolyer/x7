package io.xream.x7.demo.remote;


import io.xream.x7.demo.CatRO;
import io.xream.x7.reyc.Url;
import io.xream.x7.reyc.ReyClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import x7.core.bean.Criteria;
import x7.core.bean.condition.RefreshCondition;
import x7.core.web.ViewEntity;


@ReyClient(value = "http://${web.demo}", circuitBreaker = "", retry = true, fallback = TestFallback.class)
public interface TestServiceRemote {


    @RequestMapping(value = "/xxx/reyc/test")
    Boolean test(CatRO ro, Url url);

    @RequestMapping(value = "/xxx/reyc/test")
    Boolean testFallBack(CatRO ro);

    @RequestMapping(value = "/xxx/time/test", method = RequestMethod.GET)
    Boolean testTimeJack();

    @RequestMapping(value = "/xxx/reyc/base", method = RequestMethod.GET)
    int getBase();

    @RequestMapping("/xxx/criteria/test")
    ViewEntity testCriteria(Criteria criteria);

    @RequestMapping("/xxx/resultmap/test")
    ViewEntity testResultMap(Criteria.ResultMappedCriteria criteria);

    @RequestMapping("/xxx/domain/test")
    ViewEntity testDomain(Criteria.DomainObjectCriteria criteria);


    @RequestMapping("/xxx/refreshCondition/test")
    ViewEntity testRefreshConditionn( RefreshCondition refreshCondition);

}
