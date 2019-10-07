package com.zerofruit.bingo.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class GameManager {

    private boolean started;

    private Room room;

    private List<String> turns;

    private Integer currentTurn;

    private Integer askedNumber;

    public GameManager setup() {
        turns = new ArrayList<>();

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

    public List<BingoPlayer> getAllPlayers() {
        return this.room.findAllPlayer();
    }

    public BingoPlayer getCopartner() {
        return room.findAllPlayer()
                .stream()
                .filter(bingoPlayer -> bingoPlayer.getType().equals(PlayerType.COPARTNER))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Copoartner not exist"));
    }

    public BingoMatrix createMatrix(String clientId) {
        return new BingoMatrix(clientId);
    }

    public boolean readyToStart() {
        System.out.println("Room size is " + room.size());
        return room.size() == 5;
    }

    public List<String> setupRandomTurn() {
        List<Integer> turns = RandomNumberGenerator.range(0, 5);

        List<BingoPlayer> allPlayer = room.findAllPlayer();

        List<String> result = turns.stream()
                .map(turn -> allPlayer.get(turn).getId())
                .collect(Collectors.toList());

        this.turns = result;

        System.out.println("Random turn assigned: " + result);

        return result;
    }

    public boolean chooseNumber(String clientId, int number) {
        boolean result = room.findPlayerById(clientId).chooseNumber(number);

        room.findAllPlayer().stream().forEach(bingoPlayer -> {
            boolean r = bingoPlayer.chooseNumber(number);
            System.out.println(String.format("Result of bingo matrix marking: %b, id=%s", r, bingoPlayer.getId()));
        });
        printSpacer(clientId);

        return result;
    }

    public void askInSecret(String clientId, Integer number) {
        if (number == null) {
            System.out.println("Curprit not asked secret number");
            return;
        }
        if (!isCulpritPlayer(clientId)) {
            System.out.println("Your are not culprit, so do nothing!");
            return;
        }
        askedNumber = number;
    }

    public String isSomeoneBingo() {
        return room.findAllPlayer()
                .stream()
                .filter(BingoPlayer::isBingo)
                .findFirst()
                .map(BingoPlayer::getId)
                .orElse(null);
    }

    public boolean isPseudoPlayer(String id) {
        return room.findPlayerById(id).getType().equals(PlayerType.PSEUDO);
    }

    public boolean isCulpritPlayer(String id) {
        return room.findPlayerById(id).getType().equals(PlayerType.CULPRIT);
    }

    public void startGame() {
        this.currentTurn = 0;
        this.started = true;
    }

    public boolean isGameStarted() {
        return this.started;
    }

    public String getCurrentTurnPlayerId() {
        return turns.get(currentTurn);
    }

    public void increaseCurrentTurn() {
        currentTurn += 1;
        currentTurn %= 5;
    }

    public int doPseudoPlayerAction(String id) {
        BingoPlayer pseudoPlayer = room.findPlayerById(id);
        if (!pseudoPlayer.getType().equals(PlayerType.PSEUDO)) {
            throw new IllegalStateException("Current player is not pseudo");
        }

        boolean decided = false;
        Integer selected = null;
        List<NumberAndMarker> numberAndMarkers = pseudoPlayer.getMatrix().getNumberAndMarkers();

        List<Integer> randomIndices = RandomNumberGenerator.range(0, 25);
        for (int index : randomIndices) {
            NumberAndMarker nam = numberAndMarkers.get(index);
            if (nam.getMarker() == BingoMatrix.MARK) {
                continue;
            }

            selected = nam.getNumber();

            // this picked number should be also reflect to other players!!
            room.findAllPlayer().stream().forEach(bingoPlayer -> {
                boolean result = bingoPlayer.chooseNumber(nam.getNumber());
                System.out.println(String.format("Result of bingo matrix marking: %b, id=%s", result, bingoPlayer.getId()));
            });
            printSpacer(id);
            decided = true;
            break;
        }

        if (!decided) {
            throw new IllegalStateException("Pseudo player do not select number, Illegal state!");
        }
        if (selected == null) {
            throw new IllegalArgumentException("There's no selected number");
        }

        return selected;
    }

    private void printSpacer(String id) {
        System.out.println("###########################################");
        System.out.println(String.format("###############[%s]###################", id) );
        System.out.println("###########################################");
    }
}
