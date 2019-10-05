package com.zerofruit.bingo.server.game;

public enum PlayerType {

    PSEUDO("pseudo"), CULPRIT("culprit"), COPARTNER("copartner");

    private String name;

    private BingoMatrix bingoMatrix;

    PlayerType(String name) {
        this.name = name;
    }
}
