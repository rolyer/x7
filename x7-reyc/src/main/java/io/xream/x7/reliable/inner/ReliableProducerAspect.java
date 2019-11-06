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
package io.xream.x7.reliable.inner;

import io.xream.x7.reliable.ReliableProducer;
import io.xream.x7.reliable.api.ReliableBackend;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import x7.core.util.ExceptionUtil;

import java.util.Arrays;


@Aspect
public class ReliableProducerAspect {


    private final static Logger logger = LoggerFactory.getLogger(ReliableBackend.class);

    public ReliableProducerAspect() {
        logger.info("Reliable Producer Enabled");
    }

    @Autowired
    private ReliableBackend backend;

    @Pointcut("@annotation(io.xream.x7.reliable.ReliableProducer))")
    public void cut() {

    }

    @Around("cut() && @annotation(reliableProducer) ")
    public Object around(ProceedingJoinPoint proceedingJoinPoint, ReliableProducer reliableProducer) {

        Object[] args = proceedingJoinPoint.getArgs();
        Object body = null;
        for (Object arg : args) {
            if (arg.getClass() == reliableProducer.type()) {
                body = arg;
                break;
            }
        }

        if (body == null)
            throw new RuntimeException("ReliableMessage No Body: " + Arrays.asList(proceedingJoinPoint.getArgs()));

        Object result = this.backend.produceReliably(
                reliableProducer.isTcc(),//
                reliableProducer.topic(),//
                body,//
                reliableProducer.svcs(),//
                () -> {
                    try {
                        org.aspectj.lang.Signature signature = proceedingJoinPoint.getSignature();
                        MethodSignature ms = ((MethodSignature) signature);
                        if (ms.getReturnType() == void.class) {
                            proceedingJoinPoint.proceed();
                            return null;
                        } else {
                            return proceedingJoinPoint.proceed();
                        }
                    } catch (Throwable e) {
                        throw new RuntimeException(ExceptionUtil.getMessage(e));
                    }
                }
        );

        return result;
    }
}
