package com.zerofruit.bingo.server;

import com.zerofruit.bingo.Message;
import com.zerofruit.bingo.Method;
import com.zerofruit.bingo.game.BingoPlayer;
import com.zerofruit.bingo.game.GameManager;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
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
                         GameManager gameManager) {
        this.socket = socket;
        this.oos = oos;
        this.ois = ois;
        this.outputStreamPool = outputStreamPool;
        this.gameManager = gameManager;
    }

    public void run() {
        try {
            while (true) {
                if (gameManager.isGameStarted()) {
                    String playerId = gameManager.getCurrentTurnPlayerId();

                    System.out.println(String.format("Current turn user id is [%s]", playerId));

                    if (gameManager.isPseudoPlayer(playerId)) {
                        boolean gameIsDone = pseudoPlayerAction(playerId);
                        if (gameIsDone) {
                            break;
                        } else {
                            continue;
                        }
                    }
                }

                Message msg = (Message) ois.readObject();
                System.out.println("Received message from client: " + msg);

                oos.writeObject(handle(msg));

                if (gameManager.readyToStart() && !gameManager.isGameStarted()) {
                    setupGame();
                }
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
            Iterator<ObjectOutputStream> iter = outputStreamPool.values().iterator();
            while (iter.hasNext()) {
                ObjectOutputStream oos = iter.next();
                oos.writeObject(message);
            }
        }
        System.out.println("Broadcast message: " + message);
    }

    public void send(String id, Message message) throws IOException {
        ObjectOutputStream oos = outputStreamPool.get(id);

        oos.writeObject(message);
    }

    private Message handle(Message message) throws IOException {
        switch (message.getMethod()) {
            case Method.JOIN:
                return handleJoin(message);
            case Method.CHOOSE:
                return handleChoose(message);

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

    private Message handleChoose(Message message) throws IOException {
        gameManager.chooseNumber(message.getId(), message.getNumber());

        String winnerCandidate = gameManager.isSomeoneBingo();
        if (winnerCandidate != null) {
            broadcastWinnerMessage(winnerCandidate);

            // game done!
            System.out.println("Bingo game is finished");
        }

        broadcastUpdatedMatrixMessage(message.getNumber());

        if (gameManager.isCulpritPlayer(message.getId())) {
            gameManager.askInSecret(message.getId(), message.getSecret());
            // send secret message to copartner
            String copartnerId = gameManager.getCopartner().getId();
            send(copartnerId, Message.ofSecret(copartnerId, message.getSecret()));
        }

        gameManager.increaseCurrentTurn();

        return Message.ofChooseResult(
                this.id,
                message.getNumber()
        );
    }

    private boolean pseudoPlayerAction(String playerId) {
        boolean gameIsOver = false;

        int selected = gameManager.doPseudoPlayerAction(playerId);

        String winnerCandidate = gameManager.isSomeoneBingo();
        if (winnerCandidate != null) {
            broadcastWinnerMessage(winnerCandidate);
            gameIsOver = true;
        }

        broadcastUpdatedMatrixMessage(selected);

        gameManager.increaseCurrentTurn();

        return gameIsOver;
    }

    private void setupGame() throws IOException {
        int turn = 0;
        for (String id : gameManager.setupRandomTurn()) {
            if (!gameManager.isPseudoPlayer(id)) {
                send(id, Message.ofReadyToStart(id, turn));
            }
            turn++;
        }
        gameManager.startGame();
    }

    private void broadcastUpdatedMatrixMessage(int selected) {
        gameManager.getAllPlayers()
                .forEach(player -> {
                    if (gameManager.isPseudoPlayer(player.getId())) {
                        return;
                    }

                    try {
                        send(player.getId(), Message.ofMatrixUpdated(player.getId(), selected));
                    } catch (IOException e) {
                        System.out.println("Exception occurred while broadcastUpdatedMatrixMessage() in stream:" + e);
                    }
                });
    }

    private void broadcastWinnerMessage(String winner) {
        gameManager.getAllPlayers()
                .forEach(player -> {
                    if (gameManager.isPseudoPlayer(player.getId())) {
                        return;
                    }
                    try {
                        send(player.getId(), Message.ofWinner(player.getId(), winner));
                    } catch (IOException e) {
                        System.out.println("Exception occurred while broadcastWinnerMessage() in stream:" + e);
                    }
                });
    }

}
