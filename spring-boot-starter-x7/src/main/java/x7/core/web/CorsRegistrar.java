package x7.core.web;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import x7.EnableCorsConfig;

import java.util.Map;

public class CorsRegistrar implements ImportBeanDefinitionRegistrar {

    public static String key;


    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        Map<String, Object> attributes = annotationMetadata.getAnnotationAttributes(EnableCorsConfig.class.getName());
        key = attributes.get("key").toString();
    }
}
