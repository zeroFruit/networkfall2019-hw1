package com.zerofruit.bingo.server;

import com.zerofruit.bingo.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class ClientHandler extends Thread {
    private String id;

    private boolean init = false;

    private Socket socket;

    private Map<String, ObjectOutputStream> outputStreamPool;

    private ObjectOutputStream oos;

    private ObjectInputStream ois;

    public ClientHandler(Socket socket, Map<String, ObjectOutputStream> outputStreamPool) throws IOException, ClassNotFoundException {
        this.socket = socket;
        this.outputStreamPool = outputStreamPool;
        this.oos = new ObjectOutputStream(socket.getOutputStream());
        this.ois = new ObjectInputStream(socket.getInputStream());

        Message message = (Message) this.ois.readObject();

        System.out.println("Received first message from client: " + message);

        synchronized (outputStreamPool) {
            outputStreamPool.put(message.getId(), oos);
        }

        this.id = message.getId();
        this.init = true;
    }

    public void run() {
        try {
            while (true) {
                Message msg = (Message) ois.readObject();
                System.out.println("Received message from client: " + msg);

                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

                oos.writeObject(msg);
            }

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("connection closed...");
        } finally {
            synchronized (outputStreamPool) {
                outputStreamPool.remove(id);
            }
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void broadcast(Message message) throws IOException {
        synchronized (outputStreamPool) {
            Collection<ObjectOutputStream> col = outputStreamPool.values();
            Iterator<ObjectOutputStream> iter = col.iterator();
            while (iter.hasNext()) {
                ObjectOutputStream oos = iter.next();
                oos.writeObject(message);
            }
        }
    }
}
