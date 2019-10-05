package com.zerofruit.bingo.client;

import com.zerofruit.bingo.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class BingoClient {
    public void send(Message message) throws IOException, ClassNotFoundException {
        InetAddress host = InetAddress.getLocalHost();

        Socket socket = new Socket(host.getHostAddress(), 8888);

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
