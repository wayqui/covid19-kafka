package com.wayqui.covid19.streams;

import com.google.gson.Gson;
import com.wayqui.covid19.dto.Covid19StatDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.ValueTransformerWithKey;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.StoreBuilder;
import org.apache.kafka.streams.state.Stores;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;

@Configuration
@EnableKafka
@EnableKafkaStreams
@Slf4j
public class DailyDataGeneratorStream {

    private final String keyValueStore = "accumulate-statistics4";
    private static final String KAFKA_INPUT_TOPIC = "covid-accumulate-stats";
    private static final String KAFKA_OUTPUT_TOPIC = "covid-daily-stats";

    @Bean
    public KStream<String, String> kStream(StreamsBuilder kStreamBuilder) {
        log.info("Processing kstream...");

        StoreBuilder<KeyValueStore<String,String>> keyValueStoreBuilder =
                Stores.keyValueStoreBuilder(Stores.persistentKeyValueStore(keyValueStore),
                        Serdes.String(),
                        Serdes.String());

        kStreamBuilder.addStateStore(keyValueStoreBuilder);

        KStream<String, String> stream = kStreamBuilder
                .stream(KAFKA_INPUT_TOPIC, Consumed.with(Serdes.String(), Serdes.String()))
                .map((k, v) -> {
                    Covid19StatDto dto = new Gson().fromJson(v, Covid19StatDto.class);
                    return new KeyValue<>(dto.getCountryCode(), v);
                })
                .transformValues(() -> new ValueTransformerWithKey<String, String, String>() {

                    private KeyValueStore<String, String> state;

            @Override
            public void init(ProcessorContext processorContext) {
                state = (KeyValueStore<String, String>) processorContext.getStateStore(keyValueStore);}

            @Override
            public String transform(String key, String value) {
                String newValue = value;

                if (state.get(key) != null) {
                    Covid19StatDto prevDto = new Gson().fromJson(state.get(key), Covid19StatDto.class);
                    Covid19StatDto dto = new Gson().fromJson(value, Covid19StatDto.class);

                    dto.setConfirmed(dto.getConfirmed() - prevDto.getConfirmed());
                    dto.setActive(dto.getActive() - prevDto.getActive());
                    dto.setDeaths(dto.getDeaths() - prevDto.getDeaths());

                    newValue = new Gson().toJson(dto);
                }
                state.put(key, value);
                return newValue;
            }

            @Override
            public void close() {

            }

        }, keyValueStore);

        stream.to(KAFKA_OUTPUT_TOPIC, Produced.with(Serdes.String(), Serdes.String()));

        return stream;
    }
}
