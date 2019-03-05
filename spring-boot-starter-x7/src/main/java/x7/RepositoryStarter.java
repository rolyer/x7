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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import x7.config.SpringHelper;
import x7.core.config.ConfigAdapter;
import x7.core.config.Configs;
import x7.repository.RepositoryBooter;

import javax.sql.DataSource;
import java.util.Objects;

@EnableConfigurationProperties({
        DataSourceProperties_R.class})
public class RepositoryStarter  {

    private Logger logger = LoggerFactory.getLogger(RepositoryStarter.class);
    @Autowired
    private DataSourceProperties_R dataSourceProperties_r;
    @Autowired
    private Environment environment;
    @Autowired
    private DataSource dataSource;

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


        if (Objects.isNull(dataSourceProperties_r.getUrl())) {
            logger.info("Readable DataSource Config Key: spring.datasource.read.url");
            logger.info("Readable DataSource Config Value: null");
            logger.info("Readable DataSource Ignored");

            return null;
        }

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


        logger.info("Readable DataSource Created, url:"+dataSourceProperties_r.getUrl());
        return dsR;
    }


    public void startX7Repsository(DataSource dsW, DataSource dsR) {//FIXME

        if (Objects.isNull(dsW))
            throw new RuntimeException("Writeable DataSource Got NULL");

        if (Configs.isTrue("x7.repository.show-sql")
                || Configs.isTrue("x7.repository.showSql")
                || Configs.isTrue("spring.jpa.show-sql")
                || "debug".equals(Configs.getString("log4j.logger.org.springframework.jdbc.core.JdbcTemplate"))
                || "info".equals(Configs.getString("log4j.logger.org.springframework.jdbc.core.JdbcTemplate"))
        ) {
            ConfigAdapter.setIsShowSql(true);
        }else{
            logger.info("X7 Repsository will not show SQL, for no config like one of: x7.repository.show-sql=true,spring.jpa.show-sql=true,log4j.logger.org....." );
        }

        String driverClassName = Configs.getString("spring.datasource.driver-class-name");

        RepositoryBooter.onDriver(driverClassName);
        RepositoryBooter.boot(dsW,dsR);

    }

}
