package com.zerofruit.bingo;

import com.zerofruit.bingo.client.BingoClient;
import com.zerofruit.bingo.server.BingoServer;
import com.zerofruit.bingo.server.game.GameManager;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;

public class BingoApplication extends Application {

    static BingoClient bingoClient;

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        String mode = args[0];

        if (mode.equalsIgnoreCase("server")) {
            new BingoServer(new GameManager()).run();
        } else { // client
            new BingoClient().send(Message.ofClient("asdf", "method", 1, 1));
//            launch();
        }

    }

    @Override
    public void start(Stage stage) throws Exception {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(5);
        gridPane.setHgap(5);

        // Define the name text field
        final TextField idTextField = new TextField();
        idTextField.setPromptText("Enter your id");
        idTextField.setPrefColumnCount(10);
        idTextField.getText();

        GridPane.setConstraints(idTextField, 0, 0);
        gridPane.getChildren().add(idTextField);

        // Define the number text field
        final TextField numberTextField = new TextField();
        numberTextField.setPromptText("Enter your number");

        GridPane.setConstraints(numberTextField, 0 , 1);
        gridPane.getChildren().add(numberTextField);

        // Define the secret number text field
        final TextField secretTextField = new TextField();
        secretTextField.setPromptText("Enter your secret to send to copartner");

        GridPane.setConstraints(secretTextField, 1, 1);
        gridPane.getChildren().add(secretTextField);

        // Define the login button
        Button loginBtn = new Button("Join Room");
        GridPane.setConstraints(loginBtn, 1, 0);
        gridPane.getChildren().add(loginBtn);

        // Define the submit button
        Button submitBtn = new Button("Submit");
        GridPane.setConstraints(submitBtn, 2, 1);
        gridPane.getChildren().add(submitBtn);

        // Define list view
        ListView<String> messageList = new ListView<>();
        ObservableList<String> items = FXCollections.observableArrayList();
        messageList.setItems(items);
        messageList.setPrefWidth(200);
        messageList.setPrefHeight(200);

        GridPane.setConstraints(messageList, 0, 2);
        gridPane.getChildren().add(messageList);

        // Adding a status label
        final Label statusTitleLabel = new Label("System Status");
        GridPane.setConstraints(statusTitleLabel, 0, 3);
        GridPane.setColumnSpan(statusTitleLabel, 3);
        gridPane.getChildren().add(statusTitleLabel);

        final Label statusLabel = new Label();
        GridPane.setConstraints(statusLabel, 0, 4);
        GridPane.setColumnSpan(statusLabel, 2);
        gridPane.getChildren().add(statusLabel);


        // Adding a login account login
        final Label loginStatusTitleLabel = new Label("Your ID");
        GridPane.setConstraints(loginStatusTitleLabel, 0, 5);
        GridPane.setColumnSpan(loginStatusTitleLabel, 3);
        gridPane.getChildren().add(loginStatusTitleLabel);

        final Label accountLabel = new Label();
        GridPane.setConstraints(accountLabel, 0, 6);
        GridPane.setColumnSpan(statusLabel, 2);
        gridPane.getChildren().add(accountLabel);

        // Setting an action for the login button
        loginBtn.setOnAction(event -> {
            String id = idTextField.getText();
            if (id != null && !id.isEmpty()) {
                try {
                    bingoClient.send(
                            Message.ofClient(id, "login",null, null) );
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
                statusLabel.setText(String.format("Login id is %s", id));
                accountLabel.setText(id);
                items.add(id);
                idTextField.clear();
            }
        });

        // Setting an action for the submit button
        submitBtn.setOnAction(event -> {
            if (accountLabel.getText().isEmpty()) {
                statusLabel.setText("You first need to login to bingo room");
                return;
            }

            if (numberTextField.getText().isEmpty()) {
                statusLabel.setText("You need to fill in your number");
                return;
            }
            Integer number = Integer.parseInt(numberTextField.getText());

            Integer secret = null;
            if (!secretTextField.getText().isEmpty()) {
                secret = Integer.parseInt(secretTextField.getText());
            }

            try {
                bingoClient.send(
                        Message.ofClient(accountLabel.getText(), "submit", number, secret) );
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            statusLabel.setText(String.format("Submit number is %d", number));
            items.add(number.toString());
            numberTextField.clear();
        });

        Scene scene = new Scene(gridPane, 640, 480);
        stage.setScene(scene);
        stage.show();
    }
}
