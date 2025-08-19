package model.game;

import controller.Observer;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import model.pokemon.Pokemon;


public class GameState implements Serializable {
    
    // atributos
    private Trainer player;
    private Trainer computer;
    private GameBoard board;
    private boolean isPlayerTurn = true; // controla de quem é o turno
    private int hintsRemaining = 3; // controla as dicas
    // Poderíamos adicionar mais coisas aqui no futuro, como o turno atual.

    // atributo para o observer
    // transient para evitar que a lista de observadores seja serializada
    private transient List<Observer> observers = new ArrayList<>();

    // construtor
    public GameState(Trainer player, Trainer computer, GameBoard board) {
        this.player = player;
        this.computer = computer;
        this.board = board;
    }
   
    // métodos observer
    public void addObserver(Observer observer) {
        if (observers == null) {
            observers = new ArrayList<>();
        }
        
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        if (observers != null) {
            observers.remove(observer);
        }
    }

    public void notifyObservers() {
        if (observers != null) {
            for (Observer observer : observers) {
                observer.update(this);
            }
        }
    }

    // método para verificar o fim do jogo
    public boolean isGameOver() {
        // o jogo termina quando não há mais pokémons selvagens no tabuleiro
        for (int row = 0; row < board.getSize(); row++) {
            for (int col = 0; col < board.getSize(); col++) {
                Pokemon p = board.getCellAt(row, col).getPokemon();
                if (p != null && p.isWild()) {
                    return false; // se encontrar um selvagem, o jogo não acabou
                }
            }
        }
        return true; // se o loop terminar, não há mais selvagens
    }

    // ----- Getters -----
    public Trainer getPlayer() {  return player; }
    public Trainer getComputer() { return computer; }
    public GameBoard getBoard() { return board; }
    public boolean isPlayerTurn() { return isPlayerTurn; }
    public int getHintsRemaining() { return hintsRemaining; }

    // ----- Setters -----
    public void setPlayerTurn(boolean isPlayerTurn) { this.isPlayerTurn = isPlayerTurn; }
    public void setBoard(GameBoard board) { this.board = board; }
    public void setPlayer(Trainer player) { this.player = player; }
    public void setComputer(Trainer computer) { this.computer = computer; }

    public void decrementPlayerHints() {
        if (hintsRemaining > 0) {
            this.hintsRemaining--;
        }
    }
}