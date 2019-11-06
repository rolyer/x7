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
