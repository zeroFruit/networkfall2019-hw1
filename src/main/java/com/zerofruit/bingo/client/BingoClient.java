package com.zerofruit.bingo.client;

import com.zerofruit.bingo.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class BingoClient {
    private Socket socket;

    public BingoClient() throws IOException {
        InetAddress host = InetAddress.getLocalHost();

        socket = new Socket(host.getHostAddress(), 8888);
    }

    public void send(Message message) throws IOException, ClassNotFoundException {
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

        System.out.println("Sending request to Socket Server");

        oos.writeObject(message);

        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        Message result = (Message) ois.readObject();
        System.out.println("Message received from server: " + result);

        ois.close();
        oos.close();

        System.out.println("Closing client socket");
    }
}
