package io.xream.x7;

import io.xream.x7.demo.bean.Cat;
import io.xream.x7.demo.CatRO;
import io.xream.x7.demo.controller.XxxController;
import io.xream.x7.demo.remote.TestServiceRemote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import x7.core.web.ViewEntity;


@Component
public class XxxTest {

    @Autowired
    private TestServiceRemote testServiceRemote;
    @Autowired
    private XxxController controller;

    public  void refreshByCondition() {

        Cat cat = new Cat();

        cat.setDogId(2323);
        cat.setId(122);

        ViewEntity ve = this.controller.refreshByCondition(cat);

        System.out.println("\n______Result: " + ve);

    }

    public  void test() {

        CatRO cat = new CatRO();

        ViewEntity ve = this.controller.test(cat);

        System.out.println("\n______Result: " + ve);

    }

    public  void testOne() {

        CatRO cat = new CatRO();
        cat.setRows(10);
        cat.setPage(1);



        ViewEntity ve = this.controller.testOne(cat);

        System.out.println("\n______Result: " + ve);

    }

    public void testNonPaged(){

        CatRO ro = new CatRO();

        ViewEntity ve = this.controller.nonPaged(ro);

        System.out.println("\n______Result: " + ve);
    }

    public void create(){
        this.controller.create();
    }

    public void domain(){
        this.controller.domain();
    }

    public void distinct(){

        ViewEntity ve = this.controller.distinct(null);
        System.out.println(ve);
    }

    public void testReyClient(){

        testServiceRemote.test(new CatRO());

    }
}
