/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.xream.x7.reyc;

import com.github.kristofa.brave.httpclient.BraveHttpRequestInterceptor;
import com.github.kristofa.brave.httpclient.BraveHttpResponseInterceptor;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.retry.RetryRegistry;
import io.xream.x7.reyc.internal.ClientResolver;
import io.xream.x7.reyc.internal.HttpClientProperies;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@Import({HttpClientProperies.class})
public class ReyClientConfig {

    public ReyClientConfig(HttpClientProperies properies, CircuitBreakerRegistry circuitBreakerRegistry){

        RetryRegistry retryRegistry = RetryRegistry.ofDefaults();
        ClientResolver.init(properies, circuitBreakerRegistry, retryRegistry);

    }

    @ConditionalOnBean({BraveHttpRequestInterceptor.class,BraveHttpResponseInterceptor.class})
    @Bean
    public ReyTracing enableReyTracing(BraveHttpRequestInterceptor req,BraveHttpResponseInterceptor rep){
        ClientResolver.initInterceptor(req,  rep);
        return new ReyTracing();
    }
}
