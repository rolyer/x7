package x7;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import({L2CacheStarter.class})
public @interface EnableX7L2Cache {

    int timeSeconds() default 60;

}
