package com.zerofruit.bingo.client;

import com.zerofruit.bingo.Message;
import com.zerofruit.bingo.Method;

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

                System.out.println(String.format("Handled message ! [%s]", message.getMethod()));
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
            case Method.JOIN_RESP:
                synchronized (playerInfo) {
                    playerInfo = playerInfo
                            .id(message.getId())
                            .role(message.getRole())
                            .bingoMatrix(message.getBingoMatrix());
                }
                break;
            case Method.GAME_START:
                synchronized (playerInfo) {
                    playerInfo = playerInfo
                            .gameStarted(true)
                            .turn(message.getTurn());
                }
                break;
            case Method.MATRIX_UPDATED:
                synchronized (playerInfo) {
                    System.out.println("Selected at this turn: " + message.getSelected());
                    playerInfo = playerInfo
                            .selected(message.getSelected());

                }
                break;
            case Method.CHOOSE_RESP:
                System.out.println("Successfully choose bingo number:" + message.getNumber());
                break;
            case Method.SECRET:
                synchronized (playerInfo) {
                    playerInfo = playerInfo
                            .secret(message.getSecret());
                }
                break;
            case Method.WINNER:
                synchronized (playerInfo) {
                    playerInfo = playerInfo
                            .winner(message.getWinner());
                }
                break;
            default:
                throw new IllegalArgumentException(
                        String.format("unexpected method, got [%s]", message.getMethod()) );
        }
    }
}
