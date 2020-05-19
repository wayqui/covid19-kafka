package com.wayqui.covid19.kafka;

import com.google.gson.Gson;
import com.wayqui.covid19.dto.Covid19StatDto;
import com.wayqui.covid19.service.StatisticsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class StatisticsConsumerImpl implements StatisticsConsumer {

    @Autowired
    private StatisticsService service;

    @Override
    @KafkaListener(topics = "covid-daily-stats")
    public void onMessage(ConsumerRecord<Integer, String> record) {
        log.info("onMessage {} -> {}", record.key(), record.value());

        Covid19StatDto statisticDto = new Gson().fromJson(record.value(), Covid19StatDto.class);

        service.insertStatistic(statisticDto);

    }
}
