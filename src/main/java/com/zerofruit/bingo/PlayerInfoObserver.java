package com.zerofruit.bingo;

import javafx.application.Platform;
import javafx.scene.control.Label;
import lombok.Setter;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

@Setter
public class PlayerInfoObserver implements PropertyChangeListener {
    private Label roleLabel;
    private Label gameStartLabel;
    private Label matrixLabel;
    private Label turnLabel;
    private Label secretLabel;
    private Label winnerLabel;

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case "role":
                Platform.runLater(() -> roleLabel.setText(evt.getNewValue().toString()));
                break;
            case "bingoMatrix":
                System.out.println("at observer: " + evt.getNewValue().toString());
                Platform.runLater(() -> matrixLabel.setText(evt.getNewValue().toString()));
                break;
            case "gameStarted":
                Platform.runLater(() -> gameStartLabel.setText(evt.getNewValue().toString()));
                break;
            case "turn":
                Platform.runLater(() -> turnLabel.setText(((Integer) evt.getNewValue()).toString()));
                break;
            case "secret":
                Platform.runLater(() -> secretLabel.setText(((Integer) evt.getNewValue()).toString()));
                break;
            case "winner":
                Platform.runLater(() -> winnerLabel.setText((String) evt.getNewValue()));
            default:
                return;
        }
    }
}
