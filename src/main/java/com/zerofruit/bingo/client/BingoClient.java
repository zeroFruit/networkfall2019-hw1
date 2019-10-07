package com.zerofruit.bingo.client;

import com.zerofruit.bingo.Message;
import com.zerofruit.bingo.PlayerInfoObserver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class BingoClient {
    private Socket socket;

    private ObjectOutputStream oos;

    private ObjectInputStream ois;

    private DataHandler handler;

    private PlayerInfo playerInfo;

    public BingoClient(PlayerInfo playerInfo) throws IOException {
        socket = new Socket("localhost", 8888);
        ois = new ObjectInputStream(socket.getInputStream());
        oos = new ObjectOutputStream(socket.getOutputStream());

        this.playerInfo = playerInfo;

        this.handler = new DataHandler(socket, ois, playerInfo, this);
        this.handler.start();

        System.out.println("DataHandler running...");
    }

    public void send(Message message) throws IOException {
        System.out.println("Sending request to Socket Server");

        oos.writeObject(message);
    }

    public void close() {
        try {
            ois.close();
            oos.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    synchronized public PlayerInfo getPlayerInfo() {
        return playerInfo;
    }
}
