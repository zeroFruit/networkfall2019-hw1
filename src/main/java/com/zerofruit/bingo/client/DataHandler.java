package com.zerofruit.bingo.client;

import com.zerofruit.bingo.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class DataHandler extends Thread {
    private Socket socket;

    private ObjectInputStream ois;

    private PlayerInfo playerInfo;

    public DataHandler(Socket socket, ObjectInputStream ois, PlayerInfo playerInfo) {
        this.socket = socket;
        this.ois = ois;
        this.playerInfo = playerInfo;
    }

    public void run() {
        try {
            while (true) {
                Message message = (Message) ois.readObject();

                System.out.println("Received message from server:" + message);

                handle(message);

                System.out.println("Handled message !");
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    System.out.println("ObjectInputStream closed");
                }
            }

            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    System.out.println("Socket closed");
                }
            }
        }
    }

    private void handle(Message message) {
        switch (message.getMethod()) {
            case "join_resp":
                synchronized (playerInfo) {
                    playerInfo = playerInfo
                            .id(message.getId())
                            .role(message.getRole())
                            .bingoMatrix(message.getBingoMatrix());
                }
                break;
            case "game_start":
                synchronized (playerInfo) {
                    playerInfo = playerInfo
                            .gameStarted(true);
                }
        }
    }
}
