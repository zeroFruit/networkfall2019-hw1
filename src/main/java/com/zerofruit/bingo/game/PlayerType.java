package com.zerofruit.bingo.game;

public enum PlayerType {

    PSEUDO("pseudo"), CULPRIT("culprit"), COPARTNER("copartner");

    private String name;

    PlayerType(String name) {
        this.name = name;
    }
}
