package io.xream.x7.reyc;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface ReyClient {

    String value() default  "";

    String circuitBreaker() default "none";
}
