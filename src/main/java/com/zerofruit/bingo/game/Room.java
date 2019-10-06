package com.zerofruit.bingo.game;

import java.util.ArrayList;
import java.util.List;

public class Room {
    private List<BingoPlayer> bingoPlayers = new ArrayList<>();

    public void addPlayer(BingoPlayer bingoPlayer) {
        bingoPlayers.add(bingoPlayer);
    }

    public int size() {
        return bingoPlayers.size();
    }

    public BingoPlayer findPlayerById(String id) {
        return bingoPlayers
                .stream()
                .filter(
                        bingoPlayer -> bingoPlayer.getId().equals(id)
                )
                .findFirst()
                .orElseThrow(
                        () -> new IllegalArgumentException(String.format("player id [%s] not exist", id))
                );
    }
}
