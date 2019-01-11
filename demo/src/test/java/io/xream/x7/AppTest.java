package io.xream.x7;


import io.xream.x7.demo.Cat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import x7.core.bean.Criteria;
import x7.core.bean.CriteriaBuilder;
import x7.repository.SqlRepository;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class AppTest {

    @Autowired
    private XxxTest xxxTest;

    @Test
    public void testAll(){


        xxxTest.testOne();

    }

}
