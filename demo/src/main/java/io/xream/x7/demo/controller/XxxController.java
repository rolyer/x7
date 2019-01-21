package io.xream.x7.demo.controller;

import io.xream.x7.demo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import x7.core.bean.*;
import x7.core.bean.condition.RefreshCondition;
import x7.core.web.Page;
import x7.core.web.ViewEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/xxx")
//@Transactional
public class XxxController {

	@Autowired
	private CatTestRepository repository;// sample

	@Autowired
	private CatRepository catRepository;// sample

//	@Resource(name = "redisTemplate")
//	private RedisTemplate template;
//	@Resource(name = "stringRedisTemplate")
//	private StringRedisTemplate stringRedisTemplate;

	@RequestMapping("/create")
//	@Transactional
	public ViewEntity create(){

		Cat cat = new Cat();
		cat.setId(245);
		cat.setDogId(2);

//		this.catRepository.create(cat);

		Cat cat2 = new Cat();
		cat2.setId(246);
		cat2.setDogId(2);
//
//		this.catRepository.create(cat2);

		List<Cat> catList = new ArrayList<>();
		for (int i = 0; i< 500; i++){
			Cat cat3 = new Cat();
			cat3.setId(5448 + i);
			cat3.setDogId(2);
			catList.add(cat3);
		}
		this.catRepository.createBatch(catList);
//		throw new RuntimeException("-----------------------------> test wawawawa");

		return ViewEntity.ok();
	}

	@RequestMapping("/refresh")
	public ViewEntity refreshByCondition(@RequestBody Cat cat){


		StringRedisTemplate stringRedisTemplate = (StringRedisTemplate) SpringHelper.getObject("stringRedisTemplate");
//		stringRedisTemplate.opsForValue().set("xxx.test","ddddd");
//		String test = stringRedisTemplate.opsForValue().get("xxx.test");

//		System.out.println("_________: " + test);

		RedisTemplate template = (RedisTemplate) SpringHelper.getObject("redisTemplate");
		template.opsForValue().set("xxx.test".getBytes(),"ddddddddd".getBytes());
		Object obytes = template.opsForValue().get("xxx.test".getBytes());
		byte[] bytes = (byte[]) obytes;

		System.out.println("__________: " + new String(bytes) );

//		System.out.println("______: "  + test);
//		CriteriaBuilder builder = CriteriaBuilder.buildCondition();
//		builder.and().eq("type","NL");
//		builder.and().eq("id",2);
//
//		CriteriaCondition criteriaCondition = builder.get();

//		this.catRepository.create(cat);

		RefreshCondition<Cat> refreshCondition = new RefreshCondition<Cat>(null);
		refreshCondition.and().eq("type","NL");
		refreshCondition.refresh("test=test+1002");


		this.catRepository.refresh(cat);

//		if (true){
//			throw new RuntimeException("xxxxxxxxxxxxxxxxxxxx");
//		}

		return ViewEntity.ok();
	}

	@RequestMapping("/test")
	public ViewEntity test(@RequestBody CatRO ro) {

		{// sample, send the json by ajax from web page
			Map<String, Object> catMap = new HashMap<>();
			catMap.put("id", "");
//			catMap.put("catFriendName", "");
//			catMap.put("time", "");

			Map<String, Object> dogMap = new HashMap<>();
			dogMap.put("number", "");
			dogMap.put("userName", "");

			ro.getResultKeyMap().put("catTest", catMap);
			ro.getResultKeyMap().put("dogTest", dogMap);
		}


		String[] resultKeys = {
				"catTest.id",
				"catTest.catFriendName",
//				"dogTest.number",
				"dogTest.userName"
		};

		ro.setResultKeys(resultKeys);
//		ro.setScroll(true);

//		ro.setResultKeyMap();

		List<Object> inList = new ArrayList<>();
		inList.add("gggg");
		inList.add("xxxxx");

		CriteriaBuilder.ResultMappedBuilder builder = CriteriaBuilder.buildResultMapped(CatTest.class,ro);
//		builder.distinct("catTest.id").reduce(Reduce.ReduceType.COUNT,"catTest.id").groupBy("catTest.id");
		builder.and().in("catTest.catFriendName", inList);
		builder.paged().orderIn("catTest.catFriendName",inList);


//		builder.or().beginSub().eq("dogTest.userName","yyy")
//				.or().like("dogTest.userName",null)
//				.or().likeRight("dogTest.userName", "xxx")
//				.endSub();
//		builder.or().beginSub().eq("dogTest.userName", "uuu").endSub();
		

		String sourceScript = "catTest LEFT JOIN dogTest on catTest.dogId = dogTest.id";

		Criteria.ResultMappedCriteria resultMapped = builder.get();
		resultMapped.setSourceScript(sourceScript);

		Page<Map<String,Object>> pagination = repository.find(resultMapped);

//		Cat cat = this.catRepository.get(110);
//
//		System.out.println("____cat: " + cat);
//
//		List<Long> idList = new ArrayList<>();
//		idList.add(109L);
//		idList.add(110L);
//		InCondition inCondition = new InCondition("id",idList);
//		List<Cat> catList = this.catRepository.in(inCondition);

//		System.out.println("____catList: " + catList);

		return ViewEntity.ok(pagination);

	}



	@RequestMapping("/testOne")
	public ViewEntity testOne(@RequestBody CatRO ro) {


		String[] resultKeys = {
				"id",
				"type"
		};

//		ro.setOrderBy("cat.dogId");

		ro.setResultKeys(resultKeys);

		List<Object> inList = new ArrayList<>();
		inList.add("NL");
		inList.add("BL");

		CriteriaBuilder.ResultMappedBuilder builder = CriteriaBuilder.buildResultMapped(Cat.class,ro);
		builder.distinct("id").reduce(Reduce.ReduceType.COUNT,"dogId").groupBy("id");
		builder.and().in("type", inList);
		builder.paged().orderIn("type",inList);


//		builder.or().beginSub().eq("dogTest.userName","yyy")
//				.or().like("dogTest.userName",null)
//				.or().likeRight("dogTest.userName", "xxx")
//				.endSub();
//		builder.or().beginSub().eq("dogTest.userName", "uuu").endSub();


//		String sourceScript = "cat";

		Criteria.ResultMappedCriteria resultMapped = builder.get();

		Page<Map<String,Object>> pagination = repository.find(resultMapped);


		return ViewEntity.ok(pagination);

	}


	public ViewEntity nonPaged(@RequestBody CatRO ro) {

//		CriteriaBuilder.ResultMappedBuilder builder = CriteriaBuilder.buildResultMapped(Cat.class);
		CriteriaBuilder builder = CriteriaBuilder.build(Cat.class);

//		builder.resultKey("id").resultKey("type");
		List<Object> inList = new ArrayList<>();
		inList.add("BL");
		inList.add("NL");
		builder.and().in("type",inList);
		builder.paged().orderIn("type",inList);

//		Criteria.ResultMappedCriteria criteria = builder.get();
		Criteria criteria = builder.get();
		Page p = catRepository.find(criteria);

		return ViewEntity.ok(p);
	}


	@RequestMapping("/domain")
    public ViewEntity domain() {

		List<Long> catIdList = new ArrayList<>();
		catIdList.add(2L);
		catIdList.add(3L);

	    CriteriaBuilder.DomainObjectBuilder builder = CriteriaBuilder.buildDomainObject(Cat.class,Pig.class);

	    builder.and().in("id",catIdList);
	    builder.domain().known(catIdList).on("catId");

//		builder.domain().known(catIdList).relative(CatMouse.class).on("catId").with("mouseId");

	    Criteria.DomainObjectCriteria criteria = builder.get();

	    List<DomainObject<Cat,Mouse>> list = this.catRepository.listDomainObject(criteria);

	    System.out.println(list);

	    return ViewEntity.ok(list);
    }

}
