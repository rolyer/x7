package io.xream.x7.reliable;

import io.xream.x7.reliable.inner.ReliableOnConsumedAspect;
import io.xream.x7.reliable.inner.ReliableProducerAspect;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import({ReliableProducerAspect.class, ReliableOnConsumedAspect.class})
public @interface EnableReliabilityManagement {



}
