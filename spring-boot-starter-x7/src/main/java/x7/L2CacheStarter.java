package x7;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import x7.repository.SqlRepository;
import x7.repository.redis.LevelTwoCacheResolver;

import java.util.Map;

public class L2CacheStarter  implements ImportBeanDefinitionRegistrar {


    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        Map<String, Object> attributes = annotationMetadata.getAnnotationAttributes(EnableX7L2Cache.class.getName());

        Object obj = attributes.get("timeSeconds");

        LevelTwoCacheResolver.getInstance().setValidSecond(Integer.valueOf(obj.toString()));

        SqlRepository.getInstance().setCacheResolver(LevelTwoCacheResolver.getInstance());
    }
}
