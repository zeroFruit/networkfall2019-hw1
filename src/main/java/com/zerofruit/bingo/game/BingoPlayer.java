package com.zerofruit.bingo.game;

public interface BingoPlayer {
    PlayerType getType();
    String getId();
    BingoMatrix getMatrix();
    boolean chooseNumber(int number);
}
