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

        KStream<String, Long> confirmedPerDayStream = stream
                .map((k, v) -> {
                    Covid19StatDto dto = new Gson().fromJson(v, Covid19StatDto.class);
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    return new KeyValue<>(dto.getDate().format(formatter), dto.getConfirmed());
                });

        KStream<String, Long> recoveredPerDayStream = stream
                .map((k, v) -> {
                    Covid19StatDto dto = new Gson().fromJson(v, Covid19StatDto.class);
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    return new KeyValue<>(dto.getDate().format(formatter), dto.getRecovered());
                });

        KStream<String, Long> deathPerDayStream = stream
                .map((k, v) -> {
                    Covid19StatDto dto = new Gson().fromJson(v, Covid19StatDto.class);
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    return new KeyValue<>(dto.getDate().format(formatter), dto.getDeaths());
                });

        KStream<String, Long> confirmedPerCountryStream = stream
                .map((k, v) -> {
                    Covid19StatDto dto = new Gson().fromJson(v, Covid19StatDto.class);
                    return new KeyValue<>(dto.getCountry(), dto.getConfirmed());
                });

        KStream<String, Long> recoveredPerCountryStream = stream
                .map((k, v) -> {
                    Covid19StatDto dto = new Gson().fromJson(v, Covid19StatDto.class);
                    return new KeyValue<>(dto.getCountry(), dto.getRecovered());
                });

        KStream<String, Long> deathPerCountryStream = stream
                .map((k, v) -> {
                    Covid19StatDto dto = new Gson().fromJson(v, Covid19StatDto.class);
                    return new KeyValue<>(dto.getCountry(), dto.getDeaths());
                });

        this.aggregateStats(confirmedPerDayStream, "count-confirmed-per-day", "confirmed-per-day");
        this.aggregateStats(recoveredPerDayStream, "count-recovered-per-day", "recovered-per-day");
        this.aggregateStats(deathPerDayStream, "count-death-per-day", "death-per-day");

        this.aggregateStats(confirmedPerCountryStream, "count-confirmed-per-country", "confirmed-per-country");
        this.aggregateStats(recoveredPerCountryStream, "count-recovered-per-country", "recovered-per-country");
        this.aggregateStats(deathPerCountryStream, "count-death-per-country", "death-per-country");

        return stream;
    }

    private void aggregateStats(KStream<String, Long> kafkaStream, String materialized, String outputTopic) {
        kafkaStream
                .peek((k, v) -> log.info("{} Stream: {}=>{}", outputTopic, k, v))
                .groupByKey()
                .aggregate(
                        () -> 0L, /* initializer */
                        (aggKey, newValue, aggValue) -> aggValue + newValue, /* adder */
                        Materialized.as(materialized) /* state store name */
                )
                .toStream()
                .to(outputTopic, Produced.with(Serdes.String(), Serdes.Long()));

    }
}
