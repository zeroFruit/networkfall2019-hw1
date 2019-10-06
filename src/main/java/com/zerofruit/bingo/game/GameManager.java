package com.zerofruit.bingo.game;

public class GameManager {

    private Room room;

    public GameManager setup() {
        room = new Room();
        room.addPlayer(new PseudoBingoPlayer("pseudo1", createMatrix("pseudo1")));
        room.addPlayer(new PseudoBingoPlayer("pseudo2", createMatrix("pseudo2")));
        room.addPlayer(new PseudoBingoPlayer("pseudo3", createMatrix("pseudo3")));

        return this;
    }

    public BingoPlayer join(String id) {
        BingoPlayer player;
        if (room.size() == 3) {
            player = new CulpritPlayer(id, new BingoMatrix(id));
        } else {
            player = new CopartnerPlayer(id, new BingoMatrix(id));
        }
        room.addPlayer(player);
        return player;
    }

    public BingoMatrix createMatrix(String clientId) {
        return new BingoMatrix(clientId);
    }

    public boolean readyToStart() {
        System.out.println("Room size is " + room.size());
        return room.size() == 5;
    }

    public boolean chooseNumber(String clientId, int number) {
        BingoPlayer player = room.findPlayerById(clientId);
        return player.chooseNumber(number);
    }
}
