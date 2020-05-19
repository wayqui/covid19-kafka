package com.wayqui.covid19.controller;

import com.wayqui.covid19.conf.mapper.ObjectMapper;
import com.wayqui.covid19.controller.model.Covid19StatRequest;
import com.wayqui.covid19.dto.Covid19StatDto;
import com.wayqui.covid19.kafka.StatisticsProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class Covid19ControllerImpl implements Covid19Controller {

    @Autowired
    private StatisticsProducer producer;

    @Override
    @PostMapping("/statistics")
    public void registerStatistic(@RequestBody Covid19StatRequest request) {
        log.info("registerStatistic of {}", request.toString());

        Covid19StatDto dto = ObjectMapper.INSTANCE.requestToDto(request);

        producer.sendMessage(dto);

    }
}
