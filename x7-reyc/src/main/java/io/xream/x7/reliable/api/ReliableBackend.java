package io.xream.x7.reliable.api;

import java.util.concurrent.Callable;

public interface ReliableBackend {

    /**
     * @param isTcc
     * as regards to TCC, TCC implemented to check all resources, <br>
     *     at first, produce 'TOPIC'_TCC_TRY <br>
     *     if all resouces ok, produce 'TOPIC'_TCC_CONFIRM <br>
     *     any exception occured, produce 'TOPIC'_TCC_CANCEL <br>
     *     anyway, when isTcc = true, has to prepare 3 listeners to listener<br>
     * @param topic message topic
     * @param body  message body
     * @param svcs  the nameList of other listening domain service
     * @param callable  the service or controller handle the bisiness
     */
    Object produceReliably(Boolean isTcc, String topic, Object body, String[] svcs, Callable callable);

    /**
     *
     * @param svc  the name of listening domain service
     * @param message message body
     * @param runnable  listener handle the bisiness
     */
    void onConsumed(String svc, Object message, Runnable runnable);
}
