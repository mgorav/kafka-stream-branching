package com.gonnect.kafka.stream.cbr;

import org.apache.kafka.streams.kstream.KStream;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;

public interface KStreamProcessorWords {

    @Input("input")
    KStream<?, ?> input();

    @Output("english")
    KStream<?, ?> output1();

    @Output("french")
    KStream<?, ?> output2();

    @Output("spanish")
    KStream<?, ?> output3();
}
