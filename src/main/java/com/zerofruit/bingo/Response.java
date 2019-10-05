package com.zerofruit.bingo;

import java.io.Serializable;

public class Response implements Serializable {
    private final String id;
    private final String role;
    private final Integer number;
    private final boolean gameStart;

    public Response(String id, String role, Integer number, boolean gameStart) {
        this.id = id;
        this.role = role;
        this.number = number;
        this.gameStart = gameStart;
    }

    public String getId() {
        return id;
    }

    public String getRole() {
        return role;
    }

    public Integer getNumber() {
        return number;
    }

    public boolean isGameStart() {
        return gameStart;
    }

    @Override
    public String toString() {
        return "Response{" +
                "id='" + id + '\'' +
                ", role='" + role + '\'' +
                ", number=" + number +
                ", gameStart=" + gameStart +
                '}';
    }
}
