package com.wayqui.covid19.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class StatisticsConsumerImpl implements StatisticsConsumer {

    @Override
    @KafkaListener(topics = "covid-daily-stats")
    public void onMessage(ConsumerRecord<Integer, String> record) {
        log.info("onMessage {} -> {}", record.key(), record.value());
    }
}
