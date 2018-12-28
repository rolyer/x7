package x7;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import x7.core.bean.SpringHelper;

import java.util.Objects;


@Configuration
public class BootConfiguration {

    @Autowired
    private Environment environment;


    @Bean
    @Order(1)
    @ConditionalOnMissingBean(SpringHelper.class)
    public SpringHelper enableHelper(){
        return new SpringHelper();
    }

    @Bean
    @Order(2)
    public X7Config enableX7Config() {

        X7Env env = SpringHelper.getObject(X7Env.class);

        if (Objects.nonNull(env))
            return new X7Config();

        new X7ConfigStarter(environment);

        return new X7Config();
    }

}
