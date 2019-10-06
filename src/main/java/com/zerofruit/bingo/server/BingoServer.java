package com.zerofruit.bingo.server;

import com.zerofruit.bingo.game.GameManager;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class BingoServer {
    private GameManager gameManager;

    private Map<String, ObjectOutputStream> outputStreamPool;

    public BingoServer(GameManager gameManager) {
        this.gameManager = gameManager;
        this.outputStreamPool = new HashMap<>();
    }

    public void run() throws IOException, ClassNotFoundException {
        ServerSocket server = new ServerSocket(8888);

        try {
            while (true) {
                System.out.println("Waiting for the client request");

                Socket socket = server.accept();

                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

                System.out.println("Accepted connection request");

                ServerHandler handler = new ServerHandler(
                        socket,
                        oos,
                        ois,
                        outputStreamPool,
                        gameManager
                );
                handler.start();

                System.out.println("Running client handler...");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    private Response mux(Message message) throws IOException {
//        switch (message.getMethod()) {
//            case "login":
//                BingoPlayer bingoPlayer = gameManager.join(message.getId());
//                broadcast();
////                return new Response(
////                        message.getId(),
////                        bingoPlayer.getType().name(),
////                        null,
////                        gameManager.readyToStart()
////                );
//            default:
//                throw new IllegalArgumentException("Unexpected method");
//        }
//    }
}
