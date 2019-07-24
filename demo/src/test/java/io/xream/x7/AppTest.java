package io.xream.x7;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class AppTest {

    @Autowired
    private XxxTest xxxTest;
    @Autowired
    private TransformTest transformTest;

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


        transformTest.testCreate();
    }

}
