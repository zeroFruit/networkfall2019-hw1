package com.zerofruit.bingo;

import com.zerofruit.bingo.client.BingoClient;
import com.zerofruit.bingo.server.BingoServer;
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
    public static void main(String[] args) throws IOException, ClassNotFoundException {

        String mode = args[0];

        if (mode.equalsIgnoreCase("server")) {
            new BingoServer().run();
        } else {
            launch();
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
        idTextField.setPromptText("Entier your id");
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
        Button loginBtn = new Button("Login");
        GridPane.setConstraints(loginBtn, 1, 0);
        gridPane.getChildren().add(loginBtn);

        // Define the submit button
        Button submitBtn = new Button("Submit");
        GridPane.setConstraints(submitBtn, 2, 1);
        gridPane.getChildren().add(submitBtn);

        // Define list view
        ListView<String> messageList = new ListView<>();
        ObservableList<String> items = FXCollections.observableArrayList(
                "initialized"
        );
        messageList.setItems(items);
        messageList.setPrefWidth(200);
        messageList.setPrefHeight(300);

        GridPane.setConstraints(messageList, 0, 2);
        gridPane.getChildren().add(messageList);

        // Adding a label
        final Label label = new Label();
        GridPane.setConstraints(label, 0, 3);
        GridPane.setColumnSpan(label, 2);
        gridPane.getChildren().add(label);

        // Setting an action for the login button
        loginBtn.setOnAction(event -> {
            String id = idTextField.getText();
            if (id != null && !id.isEmpty()) {
                try {
                    new BingoClient().send(new Message(id, "login",1, null) );
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
                label.setText(String.format("Login id is %s", id));
                items.add(id);
                idTextField.clear();
            }
        });

        // Setting an action for the submit button
        submitBtn.setOnAction(event -> {
            String text = numberTextField.getText();
            if (text != null && !text.isEmpty()) {
                // TODO: do some business logic
                label.setText(String.format("Submit number is %d", Integer.parseInt(text)));
                items.add(text);
                numberTextField.clear();

            }
        });

        Scene scene = new Scene(gridPane, 640, 480);
        stage.setScene(scene);
        stage.show();
    }
}
