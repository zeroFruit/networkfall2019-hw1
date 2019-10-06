package com.zerofruit.bingo.client;

import com.zerofruit.bingo.game.BingoMatrix;
import lombok.*;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

@Data
public class PlayerInfo {

    private PropertyChangeSupport propertyChangeSupport;

    private String id;

    private String role;

    private Integer number;

    private Integer secret;

    private BingoMatrix bingoMatrix;

    private boolean gameStarted = false;

    private Integer turn;

    public PlayerInfo() {
        propertyChangeSupport = new PropertyChangeSupport(this);
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        propertyChangeSupport.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        propertyChangeSupport.removePropertyChangeListener(pcl);
    }

    public PlayerInfo id(String id) {
        this.id = id;
        return this;
    }

    public PlayerInfo role(String role) {
        propertyChangeSupport.firePropertyChange("role", this.role, role);
        this.role = role;
        return this;
    }

    public PlayerInfo number(Integer number) {
        this.number = number;
        return this;
    }

    public PlayerInfo secret(Integer secret) {
        this.secret = secret;
        return this;
    }

    public PlayerInfo bingoMatrix(BingoMatrix bingoMatrix) {
        propertyChangeSupport.firePropertyChange("bingoMatrix", this.bingoMatrix, bingoMatrix);
        this.bingoMatrix = bingoMatrix;
        return this;
    }

    public PlayerInfo gameStarted(boolean gameStarted) {
        propertyChangeSupport.firePropertyChange("gameStarted", this.gameStarted, gameStarted);
        this.gameStarted = gameStarted;
        return this;
    }

    public PlayerInfo turn(Integer turn) {
        System.out.println("Turn: " + turn);
        propertyChangeSupport.firePropertyChange("turn", this.turn, turn);
        this.turn = turn;
        return this;
    }
}
