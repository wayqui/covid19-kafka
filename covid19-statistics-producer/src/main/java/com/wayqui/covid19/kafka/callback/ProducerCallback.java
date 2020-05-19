package com.wayqui.covid19.kafka.callback;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Slf4j
public class ProducerCallback implements ListenableFutureCallback<SendResult<Long, String>> {

    private Long key;
    private String value;

    public ProducerCallback(Long key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public void onFailure(Throwable throwable) {
        log.error("Error sending message {}", throwable.getMessage());
        try {
            throw throwable;
        } catch (Throwable e) {
            log.error("Error in onFailure {}", e.getMessage());
        }
    }

    @Override
    public void onSuccess(SendResult<Long, String> sendResult) {
        log.info("This is the message {} ==> {} and the partition is {}", key, value, sendResult.getRecordMetadata().partition());
    }
}