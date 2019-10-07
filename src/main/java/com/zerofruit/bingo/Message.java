package com.zerofruit.bingo;

import com.zerofruit.bingo.game.BingoMatrix;
import lombok.*;

import java.io.Serializable;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Message implements Serializable {
    private String id;
    private String method;
    private Integer number;
    private Integer secret;
    private BingoMatrix bingoMatrix;

    private String role;
    private int turn;
    private String winner;

    private int selected;

    private Message(String id, String method) {
        this.id = id;
        this.method = method;
    }

    private Message(String id, String method, int n) {
        System.out.println("method>>" + method);
        switch (method) {
            case Method.GAME_START:
                this.id = id;
                this.method = method;
                this.turn = n;
                break;
            case Method.CHOOSE_RESP:
                this.id = id;
                this.method = method;
                this.number = n;
                break;
            case Method.SECRET:
                this.id = id;
                this.method = method;
                this.secret = n;
                break;
            case Method.MATRIX_UPDATED:
                this.id = id;
                this.method = method;
                this.selected = n;
                break;
            default:
                throw new IllegalArgumentException("invalid method");
        }
    }

    private Message(String id, String method, Integer number, Integer secret) {
        this.id = id;
        this.method = method;
        this.number = number;
        this.secret = secret;
    }

    private Message(String id, String method, String winner) {
        this.id = id;
        this.method = method;
        this.winner = winner;
    }

    private Message(String id, String method, String role, BingoMatrix bingoMatrix) {
        this.id = id;
        this.method = method;
        this.role = role;
        this.bingoMatrix = bingoMatrix;
    }

    public static Message ofJoinRequest(String id, String method) {
        return new Message(id, method);
    }

    public static Message ofJoinResult(String id, String role, BingoMatrix bingoMatrix) {
        return new Message(id, Method.JOIN_RESP, role, bingoMatrix);
    }

    public static Message ofReadyToStart(String id, int turn) {
        return new Message(id, Method.GAME_START, turn);
    }

    public static Message ofChooseRequest(String id, Integer number, Integer secret) {
        return new Message(id, Method.CHOOSE, number, secret);
    }

    public static Message ofChooseResult(String id, Integer number) {
        return new Message(id, Method.CHOOSE_RESP, number);
    }

    public static Message ofMatrixUpdated(String id, int selected) {
        return new Message(id, Method.MATRIX_UPDATED, selected);
    }

    public static Message ofSecret(String id, Integer secret) {
        return new Message(id, Method.SECRET, secret);
    }

    public static Message ofWinner(String id, String winner) {
        return new Message(id, Method.WINNER, winner);
    }
}
