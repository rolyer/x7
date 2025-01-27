package io.xream.x7;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import x7.core.bean.Criteria;
import x7.core.bean.GenericObject;
import x7.core.bean.KV;
import x7.core.util.JsonX;

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
    @Autowired
    private CatRepositoryTest repositoryTest;
    @Autowired
    private DarkRepositoryTest darkRepositoryTest;

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


//        repositoryTest.refresh();

//        xxxTest.testAlia();
//        xxxTest.distinct();

//        xxxTest.testUnion();
//        xxxTest.test();
//        xxxTest.testListCriteria();
//        xxxTest.create();
        xxxTest.refresh();

    }



}
