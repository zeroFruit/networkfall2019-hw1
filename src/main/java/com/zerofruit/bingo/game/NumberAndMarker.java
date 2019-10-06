package com.zerofruit.bingo.game;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class NumberAndMarker {
    private final int number;
    private final int marker;
}
