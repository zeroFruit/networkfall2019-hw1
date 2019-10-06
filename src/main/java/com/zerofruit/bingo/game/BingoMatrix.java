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
                    printMatrix();
                    printMarker();
                    printSpacer();
                    return true;
                }
            }
        }

        System.out.println("Not marked this turn ... :-(");
        printMatrix();
        printMarker();
        printSpacer();

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
        return sb.toString();
    }
}
