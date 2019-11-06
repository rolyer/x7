package io.xream.x7.reliable;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface ReliableProducer {

    String topic() default "";
    Class<?> type()  default Void.class;
    String[] svcs() default {};
    boolean isTcc() default false;
}
