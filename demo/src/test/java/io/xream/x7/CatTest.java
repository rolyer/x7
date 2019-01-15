package io.xream.x7;

import io.xream.x7.demo.Cat;
import x7.core.util.BeanUtil;
import x7.core.util.JsonX;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CatTest {

    public static void main(String[] args) {

        Cat cat = new Cat();
        cat.setId(1);
        cat.setDogId(2);

        Cat cat1 = new Cat();
        cat1.setId(2);
        cat1.setDogId(4);

        Cat cat3 = BeanUtil.copy(Cat.class,cat);

        List<Cat> catList = new ArrayList<>();
        catList.add(cat3);
        catList.add(cat1);

        Map<Object,Object> map = new HashMap<>();
        map.put(""+1,"test ok ok");
        map.put(""+2,"xxxx ok ok");

        PetVo vo = new PetVo();
        vo.setCat(cat);
        vo.setCatList(catList);
        vo.render(map);

        String str = JsonX.toJson(vo);
        System.out.println(str);

    }

}
