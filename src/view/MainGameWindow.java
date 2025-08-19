package view;

import controller.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import model.game.*;
import model.pokemon.Pokemon;

public class MainGameWindow extends JFrame implements Observer {

    // Atributos de dados
    private final GameBoard board;
    private final GameController gameController;
    private final Trainer player;
    private final Trainer computer;

    // Atributos de interface
    private JButton[][] cellButtons;
    private JLabel playerScoreLabel;
    private JLabel playerNameLabel;
    private JLabel computerScoreLabel;
    private JLabel computerNameLabel;
    private JButton swapPokemonButton;
    private JButton hintButton;
    private JButton exitButton;

    // Construtor
    public MainGameWindow(GameState gameState) {
        this.board = gameState.getBoard();
        this.player = gameState.getPlayer();
        this.computer = gameState.getComputer();
        this.gameController = new GameController(gameState, this);
        gameState.addObserver(this);

        setupWindow();
        setupPanels();
        setupEventListeners();

        update(gameState); // Chama update para setar o estado inicial
        setVisible(true);
    }

    // --- Métodos de Configuração da UI ---

    private void setupWindow() {
        setTitle("Pokémon - Campo de Batalha");
        setSize(800, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(10, 10, 10, 10));
    }

    private void setupPanels() {
        add(createInfoPanel(), BorderLayout.NORTH);
        add(createBoardPanel(), BorderLayout.CENTER);
        add(createActionsPanel(), BorderLayout.SOUTH);
    }

    // --- Métodos de Atualização da UI (Observer) ---

    @Override
    public void update(GameState gameState) {
        if (gameState.isGameOver()) {
            checkEndGame(gameState);
            return;
        }
        updateBoard(gameState);
        updateScoreLabels(gameState);
        updateHintButton(gameState);
        updateTurnIndicator(gameState);
    }

    private void updateScoreLabels(GameState gameState) {
        playerScoreLabel.setText("Pontuação: " + gameState.getPlayer().getScore());
        computerScoreLabel.setText("Pontuação: " + gameState.getComputer().getScore());
    }

    private void updateBoard(GameState gameState) {
        for (int row = 0; row < board.getSize(); row++) {
            for (int col = 0; col < board.getSize(); col++) {
                BoardCell cell = gameState.getBoard().getCellAt(row, col);
                JButton button = cellButtons[row][col];
                if (cell.isRevealed()) {
                    button.setEnabled(false);
                    Pokemon pokemon = cell.getPokemon();
                    button.setText(pokemon != null ? pokemon.getName() : "-");
                } else {
                    button.setEnabled(true);
                    button.setText("?");
                }
            }
        }
    }

    private void updateHintButton(GameState gameState) {
        int hints = gameState.getHintsRemaining();
        hintButton.setText("Dica (" + hints + ")");
        hintButton.setEnabled(hints > 0 && gameState.isPlayerTurn());
    }

    private void updateTurnIndicator(GameState gameState) {
        swapPokemonButton.setEnabled(gameState.isPlayerTurn());
        if (gameState.isPlayerTurn()) {
            setTitle("Pokémon - Campo de Batalha (Seu Turno)");
        } else {
            setTitle("Pokémon - Campo de Batalha (Turno do Computador)");
        }
    }

    private void checkEndGame(GameState gameState) {
        new EndGameWindow(this, gameState); // Descomente quando a EndGameWindow estiver pronta
    }

    // --- Métodos de Criação de Componentes ---

    private JPanel createInfoPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 10, 0));
        panel.setBorder(BorderFactory.createTitledBorder("Status dos Treinadores"));

        JPanel playerPanel = new JPanel(new GridLayout(2, 1));
        playerNameLabel = new JLabel("Jogador: " + player.getName());
        playerScoreLabel = new JLabel("Pontuação: " + player.getScore());
        playerPanel.add(playerNameLabel);
        playerPanel.add(playerScoreLabel);

        JPanel computerPanel = new JPanel(new GridLayout(2, 1));
        computerNameLabel = new JLabel("Adversário: " + computer.getName());
        computerScoreLabel = new JLabel("Pontuação: " + computer.getScore());
        computerPanel.add(computerNameLabel);
        computerPanel.add(computerScoreLabel);

        panel.add(playerPanel);
        panel.add(computerPanel);
        return panel;
    }

    private JPanel createBoardPanel() {
        int boardSize = board.getSize();
        JPanel boardPanel = new JPanel(new GridLayout(boardSize, boardSize));
        cellButtons = new JButton[boardSize][boardSize];
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                JButton button = new JButton("?");
                BoardCell cell = board.getCellAt(row, col);
                // Define a cor da região
                button.setBackground(switch(cell.getRegion()) {
                    case ELECTRIC -> new Color(209, 185, 14);
                    case GRASS -> new Color(48, 185, 14);
                    case GROUND -> new Color(103, 50, 9);
                    case WATER -> new Color(25, 198, 210);
                });
                button.setOpaque(true);
                cellButtons[row][col] = button;
                boardPanel.add(button);
            }
        }
        return boardPanel;
    }

    private JPanel createActionsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel centerButtonsPanel = new JPanel();
        swapPokemonButton = new JButton("Trocar Pokémon");
        hintButton = new JButton("Dica (3)");
        centerButtonsPanel.add(swapPokemonButton);
        centerButtonsPanel.add(hintButton);
        exitButton = new JButton("Sair do Jogo");
        panel.add(centerButtonsPanel, BorderLayout.CENTER);
        panel.add(exitButton, BorderLayout.EAST);
        return panel;
    }

    // --- Configuração dos Listeners ---

    private void setupEventListeners() {
        for (int row = 0; row < board.getSize(); row++) {
            for (int col = 0; col < board.getSize(); col++) {
                final int finalRow = row;
                final int finalCol = col;
                cellButtons[row][col].addActionListener(e -> gameController.handleCellClick(finalRow, finalCol));
            }
        }

        exitButton.addActionListener(e -> {
            this.dispose();
            new WelcomeWindow();
        });

        swapPokemonButton.addActionListener(e -> gameController.handleSwapPokemon());

        hintButton.addActionListener(e -> gameController.handleHintRequest());
    }
}