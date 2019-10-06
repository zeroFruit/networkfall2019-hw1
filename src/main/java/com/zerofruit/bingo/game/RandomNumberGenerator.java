package com.zerofruit.bingo.game;

import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

public class RandomNumberGenerator {
    public static List<Integer> range(int start, int end) {
        List<Integer> result = IntStream.range(start, end).boxed().collect(toList());
        Collections.shuffle(result);
        return result;
    }
}
