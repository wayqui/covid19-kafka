package com.wayqui.covid19;

import com.wayqui.covid19.api.ApiStatisticResponse;
import com.wayqui.covid19.api.StatisticsListResponse;
import com.wayqui.covid19.dto.Covid19StatDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.ZoneId;

@SpringBootApplication
@Slf4j
public class Covid19ApiClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(Covid19ApiClientApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    public CommandLineRunner run(RestTemplate restTemplate) throws Exception {
        return args -> {
            ApiStatisticResponse[] responses = restTemplate.getForObject(
                    "https://api.covid19api.com/all", ApiStatisticResponse[].class);

            if (responses != null) {
                for (ApiStatisticResponse response : responses) {
                    log.info(response.toString());

                    Covid19StatDto covid19StatDto = new Covid19StatDto();
                    covid19StatDto.setActive(response.getActive());
                    covid19StatDto.setCity(response.getCity());
                    covid19StatDto.setCityCode(response.getCityCode());
                    covid19StatDto.setConfirmed(response.getConfirmed());
                    covid19StatDto.setCountry(response.getCountry());
                    covid19StatDto.setCountryCode(response.getCountryCode());
                    covid19StatDto.setConfirmed(response.getConfirmed());
                    covid19StatDto.setDeaths(response.getDeaths());
                    covid19StatDto.setDate(response.getDate().toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDateTime());

                    restTemplate.postForObject("http://localhost:8086/covid19api/statistics", covid19StatDto, Covid19StatDto.class);

                }
            }
        };
    }

}
