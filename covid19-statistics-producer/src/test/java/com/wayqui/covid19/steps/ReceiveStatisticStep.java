package com.wayqui.covid19.steps;

import com.google.gson.Gson;
import com.wayqui.covid19.controller.model.Covid19StatRequest;
import com.wayqui.covid19.dto.Covid19StatDto;
import io.cucumber.java8.En;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;


@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EmbeddedKafka(
        topics = {"covid-accumulate-stats"},
        partitions = 1)
@TestPropertySource(
        properties = {"spring.kafka.producer.bootstrap-servers=${spring.embedded.kafka.brokers}",
                "spring.kafka.consumer.bootstrap-servers=${spring.embedded.kafka.brokers}"})
public class ReceiveStatisticStep implements En {

    public static final String POST_STATISTIC_URL = "/statistics";

    private Covid19StatRequest covid19StatRequest;

    @Autowired
    private TestRestTemplate restTemplate;

    private ResponseEntity<Object> response;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    private Consumer<String, String> consumer;

    public ReceiveStatisticStep() {

        Before(()->{
            log.info("BEFORE each scenario...");
            log.info("Setting up test, consumer and embedded kafka broker");
            Map<String, Object> config = new HashMap<>(KafkaTestUtils.consumerProps("test-group", "true", embeddedKafkaBroker));
            consumer = new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), new StringDeserializer()).createConsumer();
            embeddedKafkaBroker.consumeFromAllEmbeddedTopics(consumer);

        });

        After(()-> {
            log.info("AFTER each scenario...");
            log.info("Shutting down consumer for testing");
            consumer.close();
        });

        Given("^a statistic for a certain country and day$", () -> {
        log.info("GIVEN a statistic for a certain country and day");
        covid19StatRequest = new Covid19StatRequest().builder()
                .active(10L)
                .city("Cusco")
                .cityCode("CU")
                .confirmed(10L)
                .country("Perú")
                .countryCode("PE")
                .date(LocalDateTime.now())
                .deaths(0L)
                .latitude(50.04F)
                .longitude(50.04F)
                .province("")
                .recovered(0L)
                .build();
        });

        When("^the data is sent to the service$", () -> {
            log.info("WHEN the data is sent to the service");
            response = restTemplate
                    .postForEntity(POST_STATISTIC_URL, covid19StatRequest, Object.class);
        });

        Then("the service returns the HTTP status {string}", (String status) -> {
            log.info("THEN The service returns the HTTP status "+status);
            assertEquals(status, response.getStatusCode().getReasonPhrase());
        });

        And("^a consumer receives the message correctly$", () -> {
            log.info("AND a consumer receives the message correctly");

            ConsumerRecord<String, String> consumerRecord = KafkaTestUtils.getSingleRecord(consumer, "covid-accumulate-stats");

            Covid19StatDto statisticDto = new Gson().fromJson(consumerRecord.value(), Covid19StatDto.class);

            Assertions.assertEquals(covid19StatRequest.getActive(), statisticDto.getActive());
            Assertions.assertEquals(covid19StatRequest.getCity(), statisticDto.getCity());
            Assertions.assertEquals(covid19StatRequest.getCityCode(), statisticDto.getCityCode());
            Assertions.assertEquals(covid19StatRequest.getConfirmed(), statisticDto.getConfirmed());
            Assertions.assertEquals(covid19StatRequest.getCountry(), statisticDto.getCountry());
            Assertions.assertEquals(covid19StatRequest.getCountryCode(), statisticDto.getCountryCode());
            Assertions.assertEquals(covid19StatRequest.getDate(), statisticDto.getDate());
            Assertions.assertEquals(covid19StatRequest.getDeaths(), statisticDto.getDeaths());
            Assertions.assertEquals(covid19StatRequest.getLatitude(), statisticDto.getLatitude());
            Assertions.assertEquals(covid19StatRequest.getLongitude(), statisticDto.getLongitude());
            Assertions.assertEquals(covid19StatRequest.getProvince(), statisticDto.getProvince());
            Assertions.assertEquals(covid19StatRequest.getRecovered(), statisticDto.getRecovered());

        });
    }
}
