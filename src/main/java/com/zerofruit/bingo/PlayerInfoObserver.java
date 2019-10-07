package com.zerofruit.bingo;

import com.zerofruit.bingo.game.BingoMatrix;
import javafx.application.Platform;
import javafx.scene.control.Label;
import lombok.Setter;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Observer;

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
        System.out.println("property updated: " + evt.getPropertyName());

        switch (evt.getPropertyName()) {
            case "role":
                Platform.runLater(() -> roleLabel.setText(evt.getNewValue().toString()));
                break;
            case "bingoMatrix":
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
