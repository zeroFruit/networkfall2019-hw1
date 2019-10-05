package com.zerofruit.bingo.server.game;

public class GameManager {

    private Room room;

    public GameManager setup() {
        room = new Room();
        room.addPlayer(new PseudoBingoPlayer("pseudo1", createMatrix("pseudo1")));
        room.addPlayer(new PseudoBingoPlayer("pseudo2", createMatrix("pseudo2")));
        room.addPlayer(new PseudoBingoPlayer("pseudo3", createMatrix("pseudo3")));

        return this;
    }

    public void joinPlayer(BingoPlayer player) {
        room.addPlayer(player);
    }

    public BingoMatrix createMatrix(String clientId) {
        return new BingoMatrix(clientId);
    }

    public boolean readyToStart() {
        return room.size() == 5;
    }

    public boolean chooseNumber(String clientId, int number) {
        BingoPlayer player = room.findPlayerById(clientId);
        return player.chooseNumber(number);
    }
}
