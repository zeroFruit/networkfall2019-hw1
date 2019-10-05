package com.zerofruit.bingo.server.game;

public interface BingoPlayer {
    PlayerType getType();
    String getId();
    boolean chooseNumber(int number);
}
