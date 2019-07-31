package io.xream.x7;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@SpringBootTest
@RunWith(SpringRunner.class)
public class AppTest {

    @Autowired
    private XxxTest xxxTest;
    @Autowired
    private TransformTest transformTest;
    @Autowired
    private CatTest catTest;

    private Executor executor = Executors.newFixedThreadPool(11);

    @Test
    public void testAll(){

//        xxxTest.distinct();
//        xxxTest.testNonPaged();
//        xxxTest.domain();


//        xxxTest.testReyClient();
//        xxxTest.testTime();
//        xxxTest.getBase();

//        xxxTest.testCriteria();
//        xxxTest.testResultMapped();
//        xxxTest.testDomain();

//        xxxTest.testRefreshCondition();


//        transformTest.getOne();

        for (int i=0;  i<300; i++) {

            executor.execute(new Runnable() {
                @Override
                public void run() {
                    catTest.in();
                }
            });

        }


    }



}
