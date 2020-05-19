package com.wayqui.covid19.controller;

import com.wayqui.covid19.controller.model.Covid19StatRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class Covid19ControllerImpl implements Covid19Controller {

    @Override
    @PostMapping("/statistics")
    public void registerStatistic(Covid19StatRequest request) {
        log.info("registerStatistic");
    }
}
