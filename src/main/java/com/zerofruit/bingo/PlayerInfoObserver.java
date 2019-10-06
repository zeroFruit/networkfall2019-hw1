package com.zerofruit.bingo;

import javafx.application.Platform;
import javafx.scene.control.Label;
import lombok.Setter;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Observer;

@Setter
public class PlayerInfoObserver implements PropertyChangeListener {
    private boolean gameStarted;

    private String role;

    private Label roleLabel;
    private Label gameStartLabel;

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        System.out.println("property name: " + evt.getPropertyName());
        System.out.println("propertyChange fired: " + evt.getNewValue().toString());


        switch (evt.getPropertyName()) {
            case "role":
                Platform.runLater(() -> roleLabel.setText(evt.getNewValue().toString()));
                break;
            case "gameStarted":
                Platform.runLater(() -> gameStartLabel.setText(evt.getNewValue().toString()));
                break;
            default:
                return;
        }
    }
}
