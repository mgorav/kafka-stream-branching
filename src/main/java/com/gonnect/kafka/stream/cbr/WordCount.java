package com.gonnect.kafka.stream.cbr;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WordCount {

    private String word;

    private long count;

    private Date start;

    private Date end;
}
