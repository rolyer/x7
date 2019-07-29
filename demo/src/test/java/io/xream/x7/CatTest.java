package io.xream.x7;

import io.xream.x7.demo.CatRepository;
import io.xream.x7.demo.bean.Cat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import x7.core.bean.Reduce;
import x7.core.bean.condition.ReduceCondition;
import x7.core.bean.condition.RefreshCondition;
import x7.core.util.BeanUtil;
import x7.core.util.JsonX;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class CatTest {

    @Autowired
    private CatRepository repository;
    public void create(){

        Cat cat = new Cat();
        cat.setId(1212);
        cat.setDogId(2);

        this.repository.create(cat);

    }

    public void refresh(){

        Cat cat = new Cat();
        cat.setId(1212);
        cat.setDogId(2222);

        this.repository.refresh(cat);

    }

    public void refreshByCondition(){

        RefreshCondition<Cat> refreshCondition = new RefreshCondition<>();
        refreshCondition.refresh("type","TEST_X");
//        refreshCondition.and().eq("id",1213);
        refreshCondition.and().eq("test",433);

        this.repository.refreshUnSafe(refreshCondition);

    }

    public void remove(){

        Cat cat = new Cat();
        cat.setId(1212);
        cat.setDogId(2222);

        this.repository.remove(cat);

    }


    public Cat getOne() {
        Cat cat = new Cat();
        cat.setType("MMM");

        Cat c =  this.repository.getOne(cat);
        System.out.println(c);
        return c;
    }

    public Object reduce(){
        ReduceCondition condition = new ReduceCondition();
        condition.setReduceProperty("dogId");
        condition.setType(Reduce.ReduceType.MAX);

        Object obj = this.repository.reduce(condition);

        System.out.println(obj);

        return obj;
    }

}
