package com.zerofruit.bingo.client;

import com.zerofruit.bingo.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class DataHandler extends Thread {
    private Socket socket;

    private ObjectInputStream ois;

    public DataHandler(Socket socket, ObjectInputStream ois) {
        this.socket = socket;
        this.ois = ois;
    }

    public void run() {
        try {
            Message message = (Message) ois.readObject();

            System.out.println("Received message from server:" + message);

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
}
