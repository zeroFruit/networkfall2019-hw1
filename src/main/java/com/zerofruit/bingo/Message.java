package com.zerofruit.bingo;

import com.zerofruit.bingo.server.game.BingoMatrix;

import java.io.Serializable;

public class Message implements Serializable {
    private String id;
    private String method;
    private Integer number;
    private Integer secret;
    private BingoMatrix bingoMatrix;

    private String role;
    private boolean gameStart;

    private Message(String method) {
        this.method = method;
    }

    private Message(String id, String method, Integer number, Integer secret) {
        this.id = id;
        this.method = method;
        this.number = number;
        this.secret = secret;
    }

    private Message(String id, String role, BingoMatrix bingoMatrix) {
        this.id = id;
        this.role = role;
        this.bingoMatrix = bingoMatrix;
    }

    private Message(boolean gameStart) {
        this.gameStart = gameStart;
    }


    public static Message ofClient(String id, String method, Integer number, Integer secret) {
        return new Message(id, method, number, secret);
    }

    public static Message ofHandShake() {
        return new Message("handshake");
    }

    public static Message ofJoinResult(String id, String role, BingoMatrix bingoMatrix) {
        return new Message(id, role, bingoMatrix);
    }

    public static Message ofReadyToStart(boolean readyToStart) {
        return new Message(readyToStart);
    }

    public String getId() {
        return id;
    }

    public int getNumber() {
        return number;
    }

    public String getMethod() {
        return method;
    }

    public Integer getSecret() {
        return secret;
    }

    public String getRole() {
        return role;
    }

    public boolean isGameStart() {
        return gameStart;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id='" + id + '\'' +
                ", method='" + method + '\'' +
                ", number=" + number +
                ", secret=" + secret +
                ", role='" + role + '\'' +
                ", gameStart=" + gameStart +
                '}';
    }
}
