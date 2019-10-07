package com.zerofruit.bingo.game;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

public class BingoMatrix implements Serializable {
    static final int UNMARK = 0;
    static final int MARK = 1;

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
        List<Integer> integers = RandomNumberGenerator.range(1, 100);

        for (int r = 0; r < emptyMatrix.length; r++)  {
            for (int c = 0; c < emptyMatrix[r].length; c++) {
                emptyMatrix[r][c] = integers.get(c + r*emptyMatrix.length);
            }
        }

        return emptyMatrix;
    }

    public boolean markIfHas(int number) {
        for (int r = 0; r < matrix.length; r++)  {
            for (int c = 0; c < matrix[r].length; c++) {
                if (matrix[r][c] == number) {
                    marker[r][c] = MARK;
                    System.out.println(String.format("Marked at [%d][%d]", r, c));
//                    printMatrix();
//                    printMarker();
//                    printSpacer();
                    return true;
                }
            }
        }

        System.out.println("Not marked this turn ... :-(");
//        printMatrix();
//        printMarker();
//        printSpacer();

        return false;
    }

    public boolean isBingo() {
        // check row
        if (
                marker[0][0] == marker[0][1] && marker[0][0] == marker[0][2] && marker[0][0] == marker[0][3] && marker[0][0] == marker[0][4] && marker[0][0] == MARK ||
                marker[1][0] == marker[1][1] && marker[1][0] == marker[1][2] && marker[1][0] == marker[1][3] && marker[1][0] == marker[1][4] && marker[1][0] == MARK ||
                marker[2][0] == marker[2][1] && marker[2][0] == marker[2][2] && marker[2][0] == marker[2][3] && marker[2][0] == marker[2][4] && marker[2][0] == MARK ||
                marker[3][0] == marker[3][1] && marker[3][0] == marker[3][2] && marker[3][0] == marker[3][3] && marker[3][0] == marker[3][4] && marker[3][0] == MARK ||
                marker[4][0] == marker[4][1] && marker[4][0] == marker[4][2] && marker[4][0] == marker[4][3] && marker[4][0] == marker[4][4] && marker[4][0] == MARK
        ) {
            return true;
        }

        // check column
        if (
                marker[0][0] == marker[1][0] && marker[0][0] == marker[2][0] && marker[0][0] == marker[3][0] && marker[0][0] == marker[4][0] && marker[0][0] == MARK ||
                marker[0][1] == marker[1][1] && marker[0][1] == marker[2][1] && marker[0][1] == marker[3][1] && marker[0][1] == marker[4][1] && marker[0][1] == MARK ||
                marker[0][2] == marker[1][2] && marker[0][2] == marker[2][2] && marker[0][2] == marker[3][2] && marker[0][2] == marker[4][2] && marker[0][2] == MARK ||
                marker[0][3] == marker[1][3] && marker[0][3] == marker[2][3] && marker[0][3] == marker[3][3] && marker[0][3] == marker[4][3] && marker[0][3] == MARK ||
                marker[0][4] == marker[1][4] && marker[0][4] == marker[2][4] && marker[0][4] == marker[3][4] && marker[0][4] == marker[4][4] && marker[0][4] == MARK
        ) {
            return true;
        }

        // check diagonal
        if (
                marker[0][0] == marker[1][1] && marker[0][0] == marker[2][2] && marker[0][0] == marker[3][3] && marker[0][0] == marker[4][4] && marker[0][0] == MARK ||
                marker[0][4] == marker[1][3] && marker[0][4] == marker[2][2] && marker[0][4] == marker[3][1] && marker[0][4] == marker[4][0] && marker[0][4] == MARK
        ) {
            return true;
        }

        return false;
    }

    public List<NumberAndMarker> getNumberAndMarkers() {
        List<NumberAndMarker> result = new ArrayList<>();
        for (int r = 0; r < matrix.length; r++)  {
            for (int c = 0; c < matrix[r].length; c++) {
                result.add(new NumberAndMarker(matrix[r][c], marker[r][c]));
            }
        }
        return result;
    }

    public void printMatrix() {
        print(matrix);
    }

    public void printMarker() {
        print(marker);
    }

    public void printSpacer() {
        System.out.println("###########################################");
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n__________________________\n");
        for (int[] ints : matrix) {
            sb.append("| ");
            for (int anInt : ints) {
                sb.append(anInt + " | ");
            }
            sb.append("\n__________________________\n");
        }

        sb.append("* Current marked status\n");
        sb.append("\n__________________________\n");
        for (int[] ints : marker) {
            sb.append("| ");
            for (int anInt : ints) {
                if (anInt == MARK) {
                    sb.append("O | ");
                } else {
                    sb.append("X | ");
                }
            }
            sb.append("\n__________________________\n");
        }

        return sb.toString();
    }
}
