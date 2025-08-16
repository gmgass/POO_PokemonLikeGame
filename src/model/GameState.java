package model;

import java.io.Serializable;

/**
 * contêiner agrupa os dados do jogo.
 */
public class GameState implements Serializable {
    
    // Atributos
    private Trainer player;
    private Trainer computer;
    private GameBoard board;
    // Poderíamos adicionar mais coisas aqui no futuro, como o turno atual.

    // Construtor
    public GameState(Trainer player, Trainer computer, GameBoard board) {
        this.player = player;
        this.computer = computer;
        this.board = board;
    }

    // Getters pra recuperar os dados
    public Trainer getPlayer() {
        return player;
    }

    public Trainer getComputer() {
        return computer;
    }

    public GameBoard getBoard() {
        return board;
    }
}