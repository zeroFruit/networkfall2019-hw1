package com.zerofruit.bingo.server;

import com.zerofruit.bingo.Message;
import com.zerofruit.bingo.Response;
import com.zerofruit.bingo.server.game.BingoPlayer;
import com.zerofruit.bingo.server.game.GameManager;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BingoServer {
    private int port = 8888;

    private ServerSocket server;

    private GameManager gameManager;

    private Map<String, ObjectOutputStream> outputStreamPool;

    public BingoServer(GameManager gameManager) {
        this.gameManager = gameManager;
        this.outputStreamPool = new HashMap<>();
    }

    public void run() throws IOException, ClassNotFoundException {
        server = new ServerSocket(port);

        try {
            while (true) {
                System.out.println("Waiting for the client request");

                Socket socket = server.accept();

                System.out.println("Accepted connection request");

                ClientHandler clientHandler = new ClientHandler(socket, outputStreamPool);
                clientHandler.start();

                System.out.println("Running client handler...");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            server.close();
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
