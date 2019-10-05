package com.zerofruit.bingo.server.game;

import org.junit.Test;

public class BingoMatrixTests {
    @Test
    public void bingoMatrixConstructor() {
        new BingoMatrix("test").printMatrix();

        System.out.println("##########################");

        new BingoMatrix("test").printMarker();
    }

    @Test
    public void chooseNumberPseudoPlayer() {
        GameManager gameManager = new GameManager().setup();
        boolean result = gameManager.chooseNumber("pseudo1", 1);
        System.out.println(result);
    }
}
