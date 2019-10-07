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

    private int selected;

    private boolean gameStarted = false;

    private Integer turn;

    private String winner;

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
        propertyChangeSupport.firePropertyChange("secret", this.secret, secret);
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
        propertyChangeSupport.firePropertyChange("turn", this.turn, turn);
        this.turn = turn;
        return this;
    }

    public PlayerInfo winner(String winner) {
        propertyChangeSupport.firePropertyChange("winner", this.winner, winner);
        this.winner = winner;
        return this;
    }

    public PlayerInfo selected(int selected) {
        String oldMatrixStatus = bingoMatrix.toString();

        bingoMatrix.markIfHas(selected);

        propertyChangeSupport.firePropertyChange("bingoMatrix", oldMatrixStatus, bingoMatrix.toString());

        return this;
    }
}
