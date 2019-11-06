package io.xream.x7.reliable;

import org.mockito.internal.matchers.Null;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface ReliableProducer {

    /**
     * Tcc implemented to check all resources, if all resouces ok, produce confirm topic
     * <br>
     * Anyway, if no deed to check all resources, not value it
     */
    String topic() default "";
    Class<?> type()  default Void.class;
    String[] svcs() default {};
    boolean isTcc() default false;
}
