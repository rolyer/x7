package io.xream.x7.demo;

import io.xream.x7.demo.bean.CatTest;
import org.springframework.stereotype.Repository;

import x7.repository.BaseRepository;
import x7.repository.inner.impl.DefaultRepository;

@Repository
public class CatTestRepository extends DefaultRepository<CatTest> {

}
