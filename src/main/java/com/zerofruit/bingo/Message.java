package com.zerofruit.bingo;

import java.io.Serializable;

public class Message implements Serializable {
    private final String id;
    private final String method;
    private final Integer number;
    private final Integer secret;

    public Message(String id, String method, Integer number, Integer secret) {
        this.id = id;
        this.method = method;
        this.number = number;
        this.secret = secret;
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

    @Override
    public String toString() {
        return "Message{" +
                "id='" + id + '\'' +
                ", method='" + method + '\'' +
                ", number=" + number +
                ", secret=" + secret +
                '}';
    }
}
