package io.xream.x7.reliable.api;

import java.util.concurrent.Callable;

public interface ReliableBackend {

    /**
     * @Param topicCancel
     * @Param topicConfirm
     * as regards to topicCancel & topicConfirm, TCC implemented to check all resources, if all resouces ok, produce topicConfirm
     * <br>
     * Anyway, if no deed to check all resources, not value it
     */
    Object produceReliably(Boolean isTcc, String topic, Object body, String[] svcs, Callable callable);

    void onConsumed(String svc, Object message, Runnable runnable);
}
