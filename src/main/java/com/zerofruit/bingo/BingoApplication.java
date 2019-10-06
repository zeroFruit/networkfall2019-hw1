package com.zerofruit.bingo;

import com.zerofruit.bingo.client.BingoClient;
import com.zerofruit.bingo.client.PlayerInfo;
import com.zerofruit.bingo.game.PlayerType;
import com.zerofruit.bingo.server.BingoServer;
import com.zerofruit.bingo.game.GameManager;
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

    static PlayerInfo playerInfo;

    static PlayerInfoObserver playerInfoObserver;

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {

        String mode = args[0];

        if (mode.equalsIgnoreCase("server")) {
            new BingoServer(new GameManager().setup()).run();
        } else { // client
            playerInfo = new PlayerInfo();
            playerInfoObserver = new PlayerInfoObserver();

            bingoClient = new BingoClient(playerInfo);
            playerInfo.addPropertyChangeListener(playerInfoObserver);

            launch();

            System.out.println("BingoClient running...");
        }

    }

    @Override
    public void start(Stage stage) {
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

        // Adding labels
        final Label statusLabel = addLabel(gridPane, "[System Status]", 0, 3, 3);
        final Label accountLabel = addLabel(gridPane, "[Your ID]", 0, 5, 3);
        final Label roleLabel = addLabel(gridPane, "[Your Role]", 0, 7, 3);
        final Label matrixLabel = addLabel(gridPane, "[Your matrix]", 0, 9, 6);
        final Label gameStartLabel = addLabel(gridPane, "[Game Started?]", 0, 11, 3);
        final Label turnLabel = addLabel(gridPane, "[Your Turn is]", 0, 13, 3);

        playerInfoObserver.setRoleLabel(roleLabel);
        playerInfoObserver.setGameStartLabel(gameStartLabel);
        playerInfoObserver.setMatrixLabel(matrixLabel);
        playerInfoObserver.setTurnLabel(turnLabel);

        // Setting an action for the login button
        loginBtn.setOnAction(event -> {
            String id = idTextField.getText();
            if (id != null && !id.isEmpty()) {
                try {
                    bingoClient.send(
                            Message.ofJoinRequest(id, Method.JOIN,null, null) );
                    Thread.sleep(100);
                    System.out.println(bingoClient.getPlayerInfo().toString());
                } catch (IOException | InterruptedException e) {
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
                        Message.ofJoinRequest(accountLabel.getText(), "submit", number, secret) );
            } catch (IOException e) {
                e.printStackTrace();
            }
            statusLabel.setText(String.format("Submit number is %d", number));
            items.add(number.toString());
            numberTextField.clear();
        });

        Scene scene = new Scene(gridPane, 540, 880);
        stage.setScene(scene);
        stage.show();
    }

    private static Label addLabel(GridPane gridPane, final String labelText, int col, int row, int span) {
        final Label lh = new Label(labelText);
        GridPane.setConstraints(lh, col, row);
        GridPane.setColumnSpan(lh, span);
        gridPane.getChildren().add(lh);

        final Label l = new Label();
        GridPane.setConstraints(l, col, row + 1);
        GridPane.setColumnSpan(l, span);
        gridPane.getChildren().add(l);

        return l;
    }
}
