package io.xream.x7.reyc;

import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.xream.x7.reyc.internal.ClientResolver;
import io.xream.x7.reyc.internal.HttpClientProperies;
import org.springframework.context.annotation.Import;

@Import(HttpClientProperies.class)
public class ReyClientConfig {


    public ReyClientConfig(CircuitBreakerRegistry circuitBreakerRegistry, HttpClientProperies properies){

        ClientResolver.init(circuitBreakerRegistry,properies);
    }
}
