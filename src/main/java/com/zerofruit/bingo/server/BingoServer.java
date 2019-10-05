package com.zerofruit.bingo.server;

import com.zerofruit.bingo.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class BingoServer {
    private ServerSocket server;

    // TODO: refactor to yaml configuration
    private int port = 8888;

    public void run() throws IOException, ClassNotFoundException {
        server = new ServerSocket(port);

        while (true) {
            System.out.println("Waiting for the client request");

            Socket socket = server.accept();

            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            Message msg = (Message) ois.readObject();

            System.out.println("Received message from client: " + msg);

            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

            oos.writeObject(msg);

            ois.close();
            oos.close();

            if (msg.getId().equals("exit")) {
                break;
            }
        }

        System.out.println("Shutting down Socket server!!");

        server.close();
    }
}
