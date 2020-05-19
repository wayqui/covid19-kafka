package com.wayqui.covid19.kafka;

import com.wayqui.covid19.dto.Covid19StatDto;

public interface StatisticsProducer {
    void sendMessage(Covid19StatDto dto);
}
