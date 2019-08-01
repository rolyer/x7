package io.xream.x7;

import io.xream.x7.demo.CatRepository;
import io.xream.x7.demo.bean.Cat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CatRepositoryTest {

    @Autowired
    private CatRepository repository;

    public Cat getOne(){
        Cat cat = new Cat();
        cat.setDogId(555);
        cat = this.repository.getOne(cat);

        return cat;
    }

    public Cat get(){
        Cat cat = this.repository.get(1);
        return cat;
    }



}
