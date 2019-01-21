package io.xream.x7;


import io.xream.x7.demo.Cat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import x7.core.bean.Criteria;
import x7.core.bean.CriteriaBuilder;
import x7.core.util.JsonX;
import x7.repository.IdGenerator;
import x7.repository.SqlRepository;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class AppTest {

    @Autowired
    private XxxTest xxxTest;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void testAll(){

//        IdGenerator g1 = new IdGenerator();
//        g1.setClzName("xxx.dog");
//        g1.setMaxId(10);
//
//        String str1 = JsonX.toJson(g1);
//
//        String clzName = g1.getClass().getName();
//
//        String key = clzName+"."+g1.getClzName();
//        stringRedisTemplate.opsForValue().set(key,str1);
//        Object obj = stringRedisTemplate.opsForValue().get(key);
//        System.out.println(obj);
//
//        List<String> keyList = new ArrayList<>();
//        keyList.add(key);
//        List<String> list = stringRedisTemplate.opsForValue().multiGet(keyList);
//        System.out.println(list);

        xxxTest.create();

    }

}
