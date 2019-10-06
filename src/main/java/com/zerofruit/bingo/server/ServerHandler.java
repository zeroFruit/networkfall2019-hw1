package com.zerofruit.bingo.server;

import com.zerofruit.bingo.Message;
import com.zerofruit.bingo.server.game.BingoPlayer;
import com.zerofruit.bingo.server.game.GameManager;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class ServerHandler extends Thread {
    private String id;

    private boolean init = false;

    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    private Map<String, ObjectOutputStream> outputStreamPool;

    private GameManager gameManager;

    public ServerHandler(Socket socket,
                         ObjectOutputStream oos,
                         ObjectInputStream ois,
                         Map<String, ObjectOutputStream> outputStreamPool,
                         GameManager gameManager) throws IOException, ClassNotFoundException {
        this.socket = socket;
        this.oos = oos;
        this.ois = ois;
        this.outputStreamPool = outputStreamPool;
        this.gameManager = gameManager;

//        Message msg = (Message) ois.readObject();
//        System.out.println("Received first message from client: " + msg);
//
//        this.id = msg.getId();


//        synchronized (outputStreamPool) {
//            outputStreamPool.put(id, oos);
//        }
//
//        this.oos.writeObject(Message.ofHandShake());
//        oos.writeObject(
//                Message.ofJoinResult(
//                        this.id,
//                        bingoPlayer.getType().name(),
//                        bingoPlayer.getMatrix()));
//
//        synchronized (gameManager) {
//            if (gameManager.readyToStart()) {
//                System.out.println("Ready to start!");
//                broadcast(Message.ofReadyToStart(gameManager.readyToStart()));
//            }
//        }

    }

    public void run() {
        try {
            while (true) {
                Message msg = (Message) ois.readObject();
                System.out.println("Received message from client: " + msg);

                oos.writeObject(handle(msg));
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
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

    public void broadcast(Message message) throws IOException {
        synchronized (outputStreamPool) {
            Collection<ObjectOutputStream> col = outputStreamPool.values();
            Iterator<ObjectOutputStream> iter = col.iterator();
            while (iter.hasNext()) {
                ObjectOutputStream oos = iter.next();
                oos.writeObject(message);
            }
        }
    }

    private Message handle(Message message) {
        switch (message.getMethod()) {
            case "join":
                return handleJoin(message);
        }
        return null;
    }

    private Message handleJoin(Message message) {
        synchronized (outputStreamPool) {
            outputStreamPool.put(message.getId(), oos);
        }

        this.id = message.getId();
        this.init = true;

        // Bingo player join the room, and assigned role
        synchronized (gameManager) {
            BingoPlayer bingoPlayer = gameManager.join(this.id);
            System.out.println(String.format("Bingo player ID [%s] join the room", this.id));
            return Message.ofJoinResult(
                    this.id,
                    bingoPlayer.getType().name(),
                    bingoPlayer.getMatrix()
            );
        }

    }

}
