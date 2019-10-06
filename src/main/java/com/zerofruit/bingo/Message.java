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

    private Message(String method) {
        this.method = method;
    }

    private Message(String id, String method, int turn) {
        this.id = id;
        this.method = method;
        this.turn = turn;
    }

    private Message(String id, String method, Integer number, Integer secret) {
        this.id = id;
        this.method = method;
        this.number = number;
        this.secret = secret;
    }

    private Message(String id, String method, String role, BingoMatrix bingoMatrix) {
        this.id = id;
        this.method = method;
        this.role = role;
        this.bingoMatrix = bingoMatrix;
    }

    public static Message ofJoinRequest(String id, String method, Integer number, Integer secret) {
        return new Message(id, method, number, secret);
    }

    public static Message ofHandShake() {
        return new Message("handshake");
    }

    public static Message ofJoinResult(String id, String method, String role, BingoMatrix bingoMatrix) {
        return new Message(id, method, role, bingoMatrix);
    }

    public static Message ofReadyToStart(String id, int turn) {
        return new Message(id, "game_start", turn);
    }
}
