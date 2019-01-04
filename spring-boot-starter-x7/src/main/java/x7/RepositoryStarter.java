/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package x7;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import x7.core.bean.SpringHelper;
import x7.core.config.Configs;
import x7.repository.RepositoryBooter;
import x7.repository.RepositoryProperties;
import x7.repository.pool.HikariPoolUtil;

import javax.sql.DataSource;
import java.util.Objects;

@EnableConfigurationProperties({
		RepositoryProperties.class,
		DataSourceProperties_R.class})
public class RepositoryStarter {


	@Autowired
	private RepositoryProperties repositoryProperties;
	@Autowired
	private DataSourceProperties_R dataSourceProperties_r;
    @Autowired
    private Environment environment;
	@Autowired
	private DataSource dataSource;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Bean
    @Order(1)
    public SpringHelper enableHelper(){
        return new SpringHelper();
    }

    @Bean
    @Order(2)
    public X7Env enableX7Env() {

        new X7ConfigStarter(environment);

        return new X7Env();
    }


    @Bean
    @Order(2)
	public X7Data enableData(){

		DataSource writeDataSource = dataSource;

		/*
		 * Spring Boot多数据源不友好,另外读库可以不需要事务<br>
		 * 1. 对于Sharding, 可以用动态数据源<br>
		 * 2. 只读库，一个请求只需并成一个连接，可绕开事务<br>
		 */
		DataSource readDataSource = getReadDataSource();

		startX7Repsository(writeDataSource, readDataSource);

		return new X7Data();
	}





	public HikariDataSource getReadDataSource() {

		System.out.println("\n_________Readable DataSource Creating....");

		HikariDataSource ds = null;

		Object key = Configs.get("x7.db");
		if (Objects.nonNull(key)) {
			ds = HikariPoolUtil.create(false);
		}

		if (Objects.nonNull(ds)) {
			System.out.println("_________Readable DataSource Created By X7 Config: " + ds);
			return ds;
		}

		if (Objects.isNull(dataSourceProperties_r.getUrl())) {
			System.out.println("_________Readable DataSource Config Key: x7(x7.db.address.r) or springBoot(spring.datasource.read.url)");
			System.out.println("_________Readable DataSource Config Value: null");
			System.out.println("_________Readable DataSource Ignored\n");

			return null;
		}
//        spring.datasource.url=jdbc:mysql://127.0.0.1:3306/test?&characterEncoding=utf-8&useSSL=false
//        spring.datasource.username=root
//        spring.datasource.password=123456

		String driverClassName = Configs.getString("spring.datasource.driver-class-name");
		String username = Configs.getString("spring.datasource.username");
		String password = Configs.getString("spring.datasource.password");

		if (Objects.nonNull(dataSourceProperties_r.getDriverClassName())) {
			driverClassName = dataSourceProperties_r.getDriverClassName();
		}

		if (Objects.nonNull(dataSourceProperties_r.getUsername())) {
			username = dataSourceProperties_r.getUsername();
		}

		if (Objects.nonNull(dataSourceProperties_r.getPassword())) {
			password = dataSourceProperties_r.getPassword();
		}

		HikariDataSource dsR = new HikariDataSource();
		dsR.setJdbcUrl(dataSourceProperties_r.getUrl());
		dsR.setUsername(username);
		dsR.setPassword(password);
		dsR.setDriverClassName(driverClassName);


		System.out.println("_________Readable DataSource Created By SpringBoot Config: " + dsR);
		return dsR;
	}


	public void startX7Repsository(DataSource dsW, DataSource dsR) {//FIXME

		System.out.println("_________X7 Repsository Starter....");

		if (Objects.isNull(dsW))
			throw new RuntimeException("Writeable DataSource Got NULL");

		String driverClassName = Configs.getString("spring.datasource.driver-class-name");

		RepositoryBooter.onDriver(driverClassName);
		RepositoryBooter.boot(dsW,dsR);

	}
}
