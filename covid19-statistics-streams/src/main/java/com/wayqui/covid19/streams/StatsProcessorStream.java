package com.wayqui.covid19.streams;

import com.google.gson.Gson;
import com.wayqui.covid19.dto.Covid19StatDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;

import java.time.format.DateTimeFormatter;

@Configuration
@EnableKafka
@EnableKafkaStreams
@Slf4j
public class StatsProcessorStream {

    @Bean
    public KStream<Long, String> kStream(StreamsBuilder kStreamBuilder) {
        log.info("Processing kstream...");

        KStream<Long, String> stream = kStreamBuilder
                .stream("covid-daily-stats", Consumed.with(Serdes.Long(), Serdes.String()));

        stream
                .map((k, v) -> {
                    Covid19StatDto dto = new Gson().fromJson(v, Covid19StatDto.class);
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");                    dto.getDate().format(formatter);
                    return new KeyValue<>(dto.getDate().format(formatter), dto.getActive());
                })
                .peek((k, v) -> log.info("Before aggregate {}=>{}", k, v))
                .groupByKey()
                .aggregate(
                        () -> 0L, /* initializer */
                        (aggKey, newValue, aggValue) -> aggValue + newValue, /* adder */
                        Materialized.as("count-per-day") /* state store name */
                )
                .toStream()
                .peek((k, v) -> log.info("After aggregate {}=>{}", k, v))
                .to("confirmed-per-day", Produced.with(Serdes.String(), Serdes.Long()));

        return stream;
    }
}
