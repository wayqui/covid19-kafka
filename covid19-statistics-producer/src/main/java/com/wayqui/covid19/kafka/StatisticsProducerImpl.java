package com.wayqui.covid19.kafka;

import com.google.gson.Gson;
import com.wayqui.covid19.dto.Covid19StatDto;
import com.wayqui.covid19.kafka.callback.ProducerCallback;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

@Component
@Slf4j
public class StatisticsProducerImpl implements StatisticsProducer {

    @Autowired
    KafkaTemplate<Long, String> kafkaTemplate;

    @Override
    public void sendMessage(Covid19StatDto dto) {
        log.info("sendMessage");

        String jsonMessage = new Gson().toJson(dto);
        ListenableFuture<SendResult<Long, String>> future = kafkaTemplate.sendDefault(jsonMessage);
        future.addCallback(new ProducerCallback(null, jsonMessage));
    }
}
