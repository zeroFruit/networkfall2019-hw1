package com.zerofruit.bingo.server.game;

public class CopartnerPlayer implements BingoPlayer {
    private PlayerType type = PlayerType.COPARTNER;

    private String id;

    private BingoMatrix bingoMatrix;

    public CopartnerPlayer(String id, BingoMatrix bingoMatrix) {
        this.id = id;
        this.bingoMatrix = bingoMatrix;
    }

    @Override
    public PlayerType getType() {
        return type;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean chooseNumber(int number) {
        return bingoMatrix.markIfHas(number);
    }
}
