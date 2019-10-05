package com.zerofruit.bingo;

import java.io.Serializable;

public class Message implements Serializable {
    private final String id;
    private final int number;

    public Message(String id, int number) {
        this.id = id;
        this.number = number;
    }

    public String getId() {
        return id;
    }

    public int getNumber() {
        return number;
    }
}
