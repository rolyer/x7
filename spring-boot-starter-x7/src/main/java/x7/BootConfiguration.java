package x7;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import x7.core.bean.SpringHelper;
import x7.core.config.Configs;
import x7.repository.RepositoryProperties;
import x7.repository.pool.HikariPoolUtil;

import javax.sql.DataSource;
import java.util.Objects;

@Configuration
@EnableConfigurationProperties({
        RepositoryProperties.class,
        DataSourceProperties_R.class})
public class BootConfiguration {

    @Autowired
    private RepositoryProperties repositoryProperties;
    @Autowired
    private DataSourceProperties_R dataSourceProperties_r;
    @Autowired
    private Environment environment;
    @Autowired
    private DataSource dataSource;

    @Bean
    @Order(1)
    public SpringHelper getHelper() {
        return new SpringHelper();
    }


    private ConfigStarter x7ConfigStarter() {

        Configs.setEnvironment(environment);
        ConfigStarter configStarter = new ConfigStarter(environment.getActiveProfiles());
        RepositoryStarter.isRemote(repositoryProperties.getIsRemote());

        return configStarter;
    }


    @Bean("x7Env")
    @Qualifier("x7Env")
    public X7Env getX7Env() {

        x7ConfigStarter();

        if (repositoryProperties.getIsRemote())
            return null;

        DataSource writeDataSource = dataSource;

        /*
         * Spring Boot多数据源不友好,另外读库可以不需要事务<br>
         * 1. 对于Sharding, 可以用动态数据源<br>
         * 2. 只读库，一个请求只需并成一个连接，可绕开事务<br>
         */
        DataSource readDataSource = getReadDataSource();

        startX7Repsository(writeDataSource, readDataSource);

        return new X7Env();
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


    public void startX7Repsository(DataSource dsW, DataSource dsR) {

        System.out.println("_________X7 Repsository Starter....");

        if (Objects.isNull(dsW))
            throw new RuntimeException("Writeable DataSource Got NULL");

        String driverClassName = Configs.getString("spring.datasource.driver-class-name");

        new RepositoryStarter(dsW, dsR, driverClassName);

    }


}
