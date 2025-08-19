// Local: src/controller/GameController.java
package controller;

import java.util.Random;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import model.BoardCell;
import model.GameState;
import model.Pokemon;
import view.MainGameWindow;

public class GameController {

    //atributos
    private GameState gameState;
    private MainGameWindow view;
    private boolean isPlayerTurn = true; // O jogador humano sempre começa

    //construtor
    public GameController(GameState gameState, MainGameWindow view) {
        this.gameState = gameState;
        this.view = view;
        // Iniciar o jogo ou atualizar a view com os dados iniciais, se necessário
        // view.updateScores(gameState.getPlayer().getScore(), gameState.getComputer().getScore());
    }

    //métodos públicos (chamados pela View)

    /**
     * Lida com o clique do jogador numa célula do tabuleiro.
     * Este é o principal ponto de entrada para a lógica de uma jogada.
     */
    public void handleCellClick(int row, int col) {
        if (!isPlayerTurn) {
            // Se não for o turno do jogador, ignora o clique
            return;
        }

        BoardCell cell = gameState.getBoard().getCellAt(row, col);
        if (cell.isRevealed()) {
            return; // Ignora cliques em células já reveladas
        }

        cell.setRevealed(true);
        Pokemon foundPokemon = cell.getPokemon();
        
        // Atualiza a interface gráfica para mostrar o que foi encontrado
        view.updateCell(row, col, foundPokemon);

        if (foundPokemon != null) {
            if (foundPokemon.isWild()) {
                handleWildPokemonEncounter(foundPokemon);
            } else {
                handleTrainerBattle(foundPokemon);
            }
        } else {
            // A célula estava vazia, passa o turno
            endPlayerTurn();
        }
    }
    
    //métodos privados

    private void handleWildPokemonEncounter(Pokemon wildPokemon) {
        // Lógica de captura conforme o PDF
        int choice = JOptionPane.showConfirmDialog(view, 
            "Você encontrou um " + wildPokemon.getName() + " selvagem! Deseja tentar capturá-lo?", 
            "Pokémon Selvagem", 
            JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            boolean captured = gameState.getPlayer().capturePokemon(wildPokemon);
            if (captured) {
                // TODO: Atualizar pontuação na view
                // view.updateScores(...)
            }
        }
        endPlayerTurn();
    }

    private void handleTrainerBattle(Pokemon opponentPokemon) {
        // Lógica de batalha conforme o PDF
        JOptionPane.showMessageDialog(view, "Batalha contra " + opponentPokemon.getName() + "!");
        
        // TODO: Chamar a classe Battle para executar a batalha
        // Battle battle = new Battle(gameState.getPlayer().getMainPokemon(), opponentPokemon);
        // Pokemon winner = battle.startBattle();
        // ... distribuir experiência ...
        
        endPlayerTurn();
    }

    /**
     * Finaliza o turno do jogador e inicia o turno do computador numa nova thread.
     */
    private void endPlayerTurn() {
        this.isPlayerTurn = false;
        // TODO: Atualizar a view para indicar que é o turno do computador
        
        // Inicia a jogada do computador em uma thread separada para não travar a UI
        new Thread(() -> {
            executeComputerTurn();
        }).start();
    }

    /**
     * Contém a lógica para a jogada do computador.
     */
    private void executeComputerTurn() {
        try {
            // Simula o computador "pensando" por 1.5 segundos, como pedido no PDF
            Thread.sleep(1500);

            // Lógica simples de IA: escolher uma célula aleatória que ainda não foi revelada
            Random rand = new Random();
            int size = gameState.getBoard().getSize();
            int row, col;
            do {
                row = rand.nextInt(size);
                col = rand.nextInt(size);
            } while (gameState.getBoard().getCellAt(row, col).isRevealed());

            final int finalRow = row;
            final int finalCol = col;

            // As atualizações da UI devem ser feitas de volta na Event Dispatch Thread (EDT)
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(view, "O computador escolheu a posição (" + finalRow + ", " + finalCol + ")");
                handleComputerCellClick(finalRow, finalCol);
            });

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Lógica específica para o clique do computador.
     */
    private void handleComputerCellClick(int row, int col) {
        BoardCell cell = gameState.getBoard().getCellAt(row, col);
        cell.setRevealed(true);
        Pokemon foundPokemon = cell.getPokemon();
        view.updateCell(row, col, foundPokemon);

        // TODO: Implementar a lógica de batalha/captura para o computador
        
        // Devolve o turno para o jogador
        this.isPlayerTurn = true;
    }
}