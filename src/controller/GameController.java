// Local: src/controller/GameController.java
package controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.awt.Point;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import model.game.BoardCell;
import model.game.GameBoard;
import model.game.GameState;
import model.pokemon.Pokemon;
import view.MainGameWindow;

public class GameController {

    //atributos
    private GameState gameState;
    private MainGameWindow view;

    //construtor
    public GameController(GameState gameState, MainGameWindow view) {
        this.gameState = gameState;
        this.view = view;
        // Iniciar o jogo ou atualizar a view com os dados iniciais, se necessário
        // view.updateScores(gameState.getPlayer().getScore(), gameState.getComputer().getScore());
    }

    //métodos públicos (chamados pela View)


    public void handleCellClick(int row, int col) {
        if (!gameState.isPlayerTurn()) {
            return; // ignora o clique se não for o turno do jogador
        }

        BoardCell cell = gameState.getBoard().getCellAt(row, col);
        if (cell.isRevealed()) {
            return; // ignora cliques em células já reveladas
        }

        cell.setRevealed(true);
        Pokemon foundPokemon = cell.getPokemon();

        // lógica principal
        if (foundPokemon != null) {
            if (foundPokemon.isWild()) {
                handleWildPokemonEncounter(foundPokemon, row, col);
            } else {
                handleTrainerBattle(foundPokemon);
            }
        } else {
            // a célula estava vazia, passa o turno
            endPlayerTurn();
        }
        
        // notifica a view para se redesenhar com o novo estado
        gameState.notifyObservers();
    }
    
    //métodos privados

    private void handleWildPokemonEncounter(Pokemon wildPokemon, int row, int col) {
        // Lógica de captura conforme o PDF
        int choice = JOptionPane.showConfirmDialog(null, 
            "Você encontrou um " + wildPokemon.getName() + " selvagem! Deseja tentar capturá-lo?", 
            "Pokémon Selvagem", 
            JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            boolean captured = gameState.getPlayer().capturePokemon(wildPokemon);
            if (!captured) {
                // nova lógica de fuga!
                handlePokemonEscape(wildPokemon, row, col);
            }
        }
        endPlayerTurn();
    }

    /**
     * move um pokémon para uma célula adjacente vazia e válida.
     */
    private void handlePokemonEscape(Pokemon pokemon, int fromRow, int fromCol) {
        GameBoard board = gameState.getBoard();
        List<Point> possibleMoves = new ArrayList<>();

        // verifica as 8 posições adjacentes
        for (int r = -1; r <= 1; r++) {
            for (int c = -1; c <= 1; c++) {
                if (r == 0 && c == 0) continue; // ignora a posição atual

                int newRow = fromRow + r;
                int newCol = fromCol + c;

                // checa se a nova posição é válida
                if (newRow >= 0 && newRow < board.getSize() && newCol >= 0 && newCol < board.getSize()) {
                    BoardCell newCell = board.getCellAt(newRow, newCol);
                    // checa se a célula está vazia e se a região é compatível
                    if (newCell.getPokemon() == null && newCell.getRegion() == pokemon.getType()) {
                        possibleMoves.add(new Point(newRow, newCol));
                    }
                }
            }
        }

        if (!possibleMoves.isEmpty()) {
            // escolhe uma nova posição aleatória
            Point newPosition = possibleMoves.get(new Random().nextInt(possibleMoves.size()));
            
            // move o pokémon
            board.getCellAt(newPosition.x, newPosition.y).setPokemon(pokemon);
            board.getCellAt(fromRow, fromCol).setPokemon(null);

            JOptionPane.showMessageDialog(null, pokemon.getName() + " escapou para outra área!");
        } else {
            JOptionPane.showMessageDialog(null, pokemon.getName() + " tentou escapar, mas não tinha para onde ir!");
        }
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
        gameState.setPlayerTurn(false);
        gameState.notifyObservers(); // notifica a ui que o turno mudou

        // inicia a jogada do computador em uma nova thread para não travar a ui
        new Thread(this::executeComputerTurn).start();
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

        // TODO: Implementar a lógica de batalha/captura para o computador
        
        // devolve o turno para o jogador
        gameState.setPlayerTurn(true);
        gameState.notifyObservers(); // notifica a ui que o turno mudou de volta
    }

}