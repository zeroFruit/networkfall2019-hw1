package com.zerofruit.bingo.server.game;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

public class BingoMatrix implements Serializable {

    private static final int MARK = 1;

    private static final int MATRIX_SIZE = 5;

    private String clientId;

    private int[][] matrix;

    private int[][] marker;

    public BingoMatrix(String clientId) {
        this.clientId = clientId;

        matrix = createMatrix(new int[MATRIX_SIZE][MATRIX_SIZE]);
        marker = new int[MATRIX_SIZE][MATRIX_SIZE];
    }

    private int[][] createMatrix(int[][] emptyMatrix) {
        List<Integer> integers = IntStream.range(1, 100).boxed().collect(toList());

        Collections.shuffle(integers);

        for (int r = 0; r < emptyMatrix.length; r++)  {
            for (int c = 0; c < emptyMatrix[r].length; c++) {
                emptyMatrix[r][c] = integers.get(c + r*emptyMatrix.length);
            }
        }

        return emptyMatrix;
    }

    public boolean markIfHas(int number) {
        for (int r = 0; r < matrix.length; r++)  {
            for (int c = 0; c < marker[r].length; c++) {
                if (matrix[r][c] == number) {
                    marker[r][c] = MARK;
                    return true;
                }
            }
        }
        return false;
    }

    public void printMatrix() {
        print(matrix);
    }

    public void printMarker() {
        print(marker);
    }

    private void print(int[][] arr) {
        System.out.print("\n__________________________\n");
        for (int[] ints : arr) {
            System.out.print("| ");
            for (int anInt : ints) {
                System.out.print(anInt + " | ");
            }
            System.out.print("\n__________________________\n");
        }
    }
}
