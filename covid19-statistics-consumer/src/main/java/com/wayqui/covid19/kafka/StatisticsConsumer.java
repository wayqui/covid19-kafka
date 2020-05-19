package com.wayqui.covid19.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface StatisticsConsumer {

    void onMessage(ConsumerRecord<Integer, String> record);

}
