package com.zerofruit.bingo.client;

import com.zerofruit.bingo.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class BingoClient {
    private Socket socket;

    private ObjectOutputStream oos;

    private ObjectInputStream ois;

    private DataHandler handler;

    public BingoClient() throws IOException {
        socket = new Socket("localhost", 8888);
        ois = new ObjectInputStream(socket.getInputStream());
        oos = new ObjectOutputStream(socket.getOutputStream());

        this.handler = new DataHandler(socket, ois);
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
