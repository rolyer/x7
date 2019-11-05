package io.xream.x7.reliable.inner;

import io.xream.x7.reliable.ReliableOnConsumed;
import io.xream.x7.reliable.api.ReliableBackend;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import x7.core.util.ExceptionUtil;


@Aspect
public class ReliableOnConsumedAspect {

    private final static Logger logger = LoggerFactory.getLogger(ReliableBackend.class);


    public ReliableOnConsumedAspect() {
        logger.info("Reliable OnConsumed Enabled");
    }

    @Autowired
    private ReliableBackend backend;

    @Pointcut("@annotation(io.xream.x7.reliable.ReliableOnConsumed))")
    public void cut() {

    }

    @Around("cut() && @annotation(reliableOnConsumed) ")
    public void around(ProceedingJoinPoint proceedingJoinPoint, ReliableOnConsumed reliableOnConsumed) {

        Object[] args = proceedingJoinPoint.getArgs();
        Object message = args[0];

        String svc = reliableOnConsumed.svc();

        this.backend.onConsumed(svc, message,
                () -> {
                    try {
                        proceedingJoinPoint.proceed();

                    } catch (Throwable e) {
                        throw new RuntimeException(ExceptionUtil.getMessage(e));
                    }
                }
        );

    }
}
