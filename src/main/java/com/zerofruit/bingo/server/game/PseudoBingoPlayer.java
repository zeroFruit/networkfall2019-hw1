package com.zerofruit.bingo.server.game;

public class PseudoBingoPlayer implements BingoPlayer {

    private PlayerType type = PlayerType.PSEUDO;

    private String id;

    private BingoMatrix bingoMatrix;

    public PseudoBingoPlayer(String id, BingoMatrix bingoMatrix) {
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
    public BingoMatrix getMatrix() {
        return bingoMatrix;
    }

    @Override
    public boolean chooseNumber(int number) {
        return bingoMatrix.markIfHas(number);
    }
}
