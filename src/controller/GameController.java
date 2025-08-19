// Local: src/controller/GameController.java
package controller;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import model.battle.*;
import model.game.*;
import model.pokemon.Pokemon;
import view.BattleWindow;
import view.MainGameWindow;

public class GameController {

    private GameState gameState;
    private MainGameWindow view;

    public GameController(GameState gameState, MainGameWindow view) {
        this.gameState = gameState;
        this.view = view;
    }

    /**
     * Lida com o clique do jogador em uma célula do tabuleiro.
     * @param row A linha da célula clicada.
     * @param col A coluna da célula clicada.
     */
    public void handleCellClick(int row, int col) {
        if (!gameState.isPlayerTurn()) {
            return; // Ignora o clique se não for o turno do jogador
        }

        BoardCell cell = gameState.getBoard().getCellAt(row, col);
        if (cell.isRevealed()) {
            return; // Ignora cliques em células já reveladas
        }

        cell.setRevealed(true);
        Pokemon foundPokemon = cell.getPokemon();

        if (foundPokemon != null) {
            if (foundPokemon.isWild()) {
                handleWildPokemonEncounter(foundPokemon, row, col);
            } else {
                // Inicia a batalha se o Pokémon encontrado não pertencer ao jogador
                if (!gameState.getPlayer().getTeam().contains(foundPokemon)) {
                    handleTrainerBattle(foundPokemon);
                } else {
                     endPlayerTurn(); // Encontrou o próprio pokémon, passa o turno
                }
            }
        } else {
            // A célula estava vazia, passa o turno
            endPlayerTurn();
        }
        
        gameState.notifyObservers();
    }

    /**
     * Lida com a lógica de troca de Pokémon a pedido do jogador.
     */
    public void handleSwapPokemon() {
        Trainer player = gameState.getPlayer();

        if (!gameState.isPlayerTurn() || !player.hasPokemonInBackpack()) {
            JOptionPane.showMessageDialog(view, "Não há Pokémons na mochila para trocar!", "Troca Impossível", JOptionPane.WARNING_MESSAGE);
            return;
        }

        List<Pokemon> backpack = player.getBackpack();
        Pokemon[] options = backpack.toArray(new Pokemon[0]);

        Pokemon chosenPokemon = (Pokemon) JOptionPane.showInputDialog(
                view, "Escolha um Pokémon para ser o principal:", "Trocar Pokémon",
                JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

        if (chosenPokemon != null) {
            player.swapMainPokemon(chosenPokemon);
            gameState.notifyObservers();
        }
    }

    /**
     * Lida com a lógica de dica a pedido do jogador.
     */
    public void handleHintRequest() {
        if (!gameState.isPlayerTurn() || gameState.getHintsRemaining() <= 0) {
            return;
        }

        String[] options = {"Linha", "Coluna"};
        int choice = JOptionPane.showOptionDialog(view, "Verificar Pokémons em uma linha ou coluna?", "Dica",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        if (choice == -1) return;

        String input = JOptionPane.showInputDialog(view, "Digite o número da " + options[choice] + " (0-" + (gameState.getBoard().getSize() - 1) + "):");
        if (input == null) return;

        try {
            int index = Integer.parseInt(input);
            boolean found = false;
            
            for (int i = 0; i < gameState.getBoard().getSize(); i++) {
                Pokemon p = (choice == 0) ? gameState.getBoard().getCellAt(index, i).getPokemon()
                                          : gameState.getBoard().getCellAt(i, index).getPokemon();
                if (p != null) {
                    found = true;
                    break;
                }
            }

            String message = found ? "Sim, há pelo menos um Pokémon nesta " + options[choice] + "!"
                                   : "Não, não há Pokémons nesta " + options[choice] + ".";
            JOptionPane.showMessageDialog(view, message);

            gameState.decrementPlayerHints();
            gameState.notifyObservers();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(view, "Por favor, digite um número válido.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    // --- Métodos Privados ---

    private void handleWildPokemonEncounter(Pokemon wildPokemon, int row, int col) {
        int choice = JOptionPane.showConfirmDialog(view, 
            "Você encontrou um " + wildPokemon.getName() + " selvagem! Deseja tentar capturá-lo?", 
            "Pokémon Selvagem", 
            JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            boolean captured = gameState.getPlayer().capturePokemon(wildPokemon);
            if (!captured) {
                handlePokemonEscape(wildPokemon, row, col);
            }
        }
        endPlayerTurn();
    }
    
    private void handleTrainerBattle(Pokemon opponentPokemon) {
        Pokemon playerPokemon = gameState.getPlayer().getMainPokemon();

        if (playerPokemon == null) {
            JOptionPane.showMessageDialog(view, "Você não tem um Pokémon para batalhar!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        JOptionPane.showMessageDialog(view, "Batalha contra " + opponentPokemon.getName() + "!");
        
        Battle battle = new Battle(playerPokemon, opponentPokemon);
        BattleWindow battleView = new BattleWindow(view, battle);
        BattleResult result = battleView.showBattle();

        if (result != null) {
            if (result.wasFlee()) {
                JOptionPane.showMessageDialog(view, "Você fugiu da batalha!");
            } else {
                Pokemon winner = result.getWinner();
                JOptionPane.showMessageDialog(view, winner.getName() + " venceu a batalha!");
                gameState.getPlayer().updateScore();
                gameState.getComputer().updateScore();
            }
        }

        endPlayerTurn();
    }

    private void handlePokemonEscape(Pokemon pokemon, int fromRow, int fromCol) {
        GameBoard board = gameState.getBoard();
        List<Point> possibleMoves = new ArrayList<>();

        for (int r = -1; r <= 1; r++) {
            for (int c = -1; c <= 1; c++) {
                if (r == 0 && c == 0) continue;
                int newRow = fromRow + r;
                int newCol = fromCol + c;

                if (newRow >= 0 && newRow < board.getSize() && newCol >= 0 && newCol < board.getSize()) {
                    BoardCell newCell = board.getCellAt(newRow, newCol);
                    if (newCell.getPokemon() == null && newCell.getRegion() == pokemon.getType()) {
                        possibleMoves.add(new Point(newRow, newCol));
                    }
                }
            }
        }

        if (!possibleMoves.isEmpty()) {
            Point newPosition = possibleMoves.get(new Random().nextInt(possibleMoves.size()));
            board.getCellAt(newPosition.x, newPosition.y).setPokemon(pokemon);
            board.getCellAt(fromRow, fromCol).setPokemon(null);
            JOptionPane.showMessageDialog(view, pokemon.getName() + " escapou para outra área!");
        } else {
            JOptionPane.showMessageDialog(view, pokemon.getName() + " tentou escapar, mas não tinha para onde ir!");
        }
    }

    private void endPlayerTurn() {
        gameState.setPlayerTurn(false);
        gameState.notifyObservers();
        new Thread(this::executeComputerTurn).start();
    }

    private void executeComputerTurn() {
        try {
            Thread.sleep(1500);
            Random rand = new Random();
            int size = gameState.getBoard().getSize();
            int row, col;
            do {
                row = rand.nextInt(size);
                col = rand.nextInt(size);
            } while (gameState.getBoard().getCellAt(row, col).isRevealed());

            final int finalRow = row;
            final int finalCol = col;

            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(view, "O computador escolheu a posição (" + finalRow + ", " + finalCol + ")");
                handleComputerCellClick(finalRow, finalCol);
            });

        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }

    private void handleComputerCellClick(int row, int col) {
        BoardCell cell = gameState.getBoard().getCellAt(row, col);
        cell.setRevealed(true);
        Pokemon foundPokemon = cell.getPokemon();

        if (foundPokemon != null) {
            if(foundPokemon.isWild()){
                 JOptionPane.showMessageDialog(view, "O computador encontrou " + foundPokemon.getName() + " selvagem e o capturou!");
                 gameState.getComputer().capturePokemon(foundPokemon);
            } else if (!gameState.getComputer().getTeam().contains(foundPokemon)){
                 handleTrainerBattle(foundPokemon);
            }
        }

        gameState.setPlayerTurn(true);
        gameState.notifyObservers();
    }
}