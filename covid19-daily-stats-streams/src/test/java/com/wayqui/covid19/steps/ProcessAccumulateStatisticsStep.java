package com.wayqui.covid19.steps;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.wayqui.covid19.api.ApiStatisticResponse;
import com.wayqui.covid19.dto.Covid19StatDto;
import com.wayqui.covid19.mapper.BeanMapper;
import io.cucumber.java8.En;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.TestInputTopic;
import org.apache.kafka.streams.TestOutputTopic;
import org.apache.kafka.streams.TopologyTestDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;

import java.io.FileReader;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.test.util.AssertionErrors.assertEquals;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
/*@EmbeddedKafka(
        topics = {"covid-accumulate-stats"},
        partitions = 1)
@TestPropertySource(
        properties = {"spring.kafka.producer.bootstrap-servers=${spring.embedded.kafka.brokers}",
                "spring.kafka.consumer.bootstrap-servers=${spring.embedded.kafka.brokers}"})

*/
public class ProcessAccumulateStatisticsStep implements En {

    @Autowired
    private StreamsBuilderFactoryBean streamsBuilderFactoryBean;

    private TopologyTestDriver testDriver;

    private TestInputTopic<String, String> inputTopic;
    private TestOutputTopic<String, String> outputTopic;

    private List<Covid19StatDto> covid19StatDtos;

    private List<KeyValue<String, String>> dailyStatsResult;

    public ProcessAccumulateStatisticsStep() {

        Before(()->{
            log.info("BEFORE each scenario...");
            log.info("Setting up topology test driver");

            testDriver = new TopologyTestDriver(streamsBuilderFactoryBean.getTopology(),
                    Objects.requireNonNull(streamsBuilderFactoryBean.getStreamsConfiguration()));

            inputTopic = testDriver.createInputTopic("covid-accumulate-stats",
                    Serdes.String().serializer(),
                    Serdes.String().serializer()
            );

            outputTopic = testDriver.createOutputTopic("covid-daily-stats",
                    Serdes.String().deserializer(),
                    Serdes.String().deserializer()
            );
        });

        Given("a list of daily statistics with accumulate data", () -> {
            Gson gson = new Gson();
            JsonReader reader = new JsonReader(new FileReader("src/test/resources/com/wayqui/covid19/data/testData.json"));
            ApiStatisticResponse[] data = gson.fromJson(reader, ApiStatisticResponse[].class); // contains the whole reviews list

            List<ApiStatisticResponse> responses = Arrays.asList(data);

            covid19StatDtos = BeanMapper.INSTANCE.apiResponsesToDtos(responses);
        });

        When("the stream processes the message", () -> {
            for (Covid19StatDto covid19StatDto  : covid19StatDtos) {
                inputTopic.pipeInput(covid19StatDto.getCountryCode(), new Gson().toJson(covid19StatDto));
            }

            // Write code here that turns the phrase above into concrete actions
            //throw new io.cucumber.java8.PendingException();
        });

        When("the new message is sent to the destination topic", () -> {
            // Write code here that turns the phrase above into concrete actions
            //throw new io.cucumber.java8.PendingException();
        });

        Then("a consumer receives the message correctly", () -> {
            dailyStatsResult = outputTopic.readKeyValuesToList();

            assertEquals("The number of messages are not the same", covid19StatDtos.size(), dailyStatsResult.size());
        });

        And("the daily statistics were calculated correctly", () -> {

            Covid19StatDto lastStatistic = covid19StatDtos.get(covid19StatDtos.size() - 1);
            log.info(lastStatistic.getStatistics());

            AtomicReference<Long> totalRecovered = new AtomicReference<>(0L);
            AtomicReference<Long> totalConfirmed = new AtomicReference<>(0L);
            AtomicReference<Long> totalDead = new AtomicReference<>(0L);

            dailyStatsResult.forEach(result -> {
                Covid19StatDto resultDto = new Gson().fromJson(result.value, Covid19StatDto.class);
                totalConfirmed.updateAndGet(v -> v + resultDto.getConfirmed());
                totalRecovered.updateAndGet(v -> v + resultDto.getRecovered());
                totalDead.updateAndGet(v -> v + resultDto.getDeaths());
            });
            log.info("{ totalConfirmed="+totalConfirmed.get()+", " +
                    "totalRecovered="+totalRecovered.get()+", " +
                    "totalDead="+totalDead.get()+"}");

            /*assertEquals("Confirmed cases didn't match", lastStatistic.getConfirmed(), totalConfirmed.get());
            assertEquals("Recovered cases didn't match", lastStatistic.getRecovered(), totalRecovered.get());
            assertEquals("Dead cases didn't match", lastStatistic.getDeaths(), totalDead.get());
*/

            HashMap<String, Covid19StatDto> mapResult = dailyStatsResult
                    .stream()
                    .collect(HashMap::new,
                            (m, c) -> m.put(c.key, new Gson().fromJson(c.value, Covid19StatDto.class)),
                            (m, u) -> {});

            Map.Entry<String, Covid19StatDto> result = Collections
                    .max(mapResult.entrySet(),
                            Comparator.comparing(entry -> entry.getValue().getDate()));



        });

        After(()-> {
            log.info("AFTER each scenario...");
            log.info("Shutting down topology test driver");
            testDriver.close();
            streamsBuilderFactoryBean.getKafkaStreams().close();
        });

    }
}
