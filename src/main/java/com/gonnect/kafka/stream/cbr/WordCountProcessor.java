package com.gonnect.kafka.stream.cbr;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Predicate;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.SendTo;

import java.util.Arrays;
import java.util.Date;

@EnableBinding(KStreamProcessorWords.class)
@RequiredArgsConstructor
@Log4j2
public class WordCountProcessor {

    private final TimeWindows timeWindows;


    @StreamListener("input")
    @SendTo({"english", "french", "spanish"})
    @SuppressWarnings("unchecked")
    public KStream<?, WordCount>[] process(KStream<Object, String> input) {

        Predicate<Object, WordCount> isEnglish = (k, v) -> v.getWord().equals("english");
        Predicate<Object, WordCount> isFrench = (k, v) -> v.getWord().equals("french");
        Predicate<Object, WordCount> isSpanish = (k, v) -> v.getWord().equals("spanish");


        return input
                .flatMapValues(value -> Arrays.asList(value.toLowerCase().split("\\W+")))
                .groupBy((key, value) -> value)
                .windowedBy(timeWindows)
                .count(Materialized.as("WordCounts"))
                .toStream()
                .map((key, value) -> new KeyValue<>(null,
                        new WordCount(key.key(), value, new Date(key.window().start()), new Date(key.window().end()))))
                .branch(isEnglish, isFrench, isSpanish);
    }
}
