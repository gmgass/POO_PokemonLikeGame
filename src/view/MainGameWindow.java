package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import model.game.*;
import model.pokemon.Pokemon;

import controller.*;
// import controller.GameController; // Descomente quando a classe for criada

public class MainGameWindow extends JFrame implements Observer{

    //atributos de dados
    private final GameBoard board;
    private final GameController gameController;
    private final Trainer player; // Adicionado
    private final Trainer computer; // O cérebro do jogo

    //atributos de interface
    private JButton[][] cellButtons;
    private JLabel playerScoreLabel;
    private JLabel playerNameLabel;
    private JLabel computerScoreLabel;
    private JLabel computerNameLabel;
    private JButton swapPokemonButton;
    private JButton hintButton;
    private JButton exitButton;

    //construtor
    public MainGameWindow(GameState gameState) {
        
        //inicializa atributos
        this.board = gameState.getBoard();  
        this.player = gameState.getPlayer();
        this.computer = gameState.getComputer();
        this.gameController = new GameController(gameState, this); // cria o controlador do jogo
        gameState.addObserver(this);

        //monta a janela
        setTitle("Pokémon - Campo de Batalha");
        setSize(800, 600); // Padronizado com as outras janelas
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // adiciona uma margem interna à janela
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(10, 10, 10, 10));

        // cria e adiciona os painéis principais
        JPanel infoPanel = createInfoPanel();
        JPanel boardPanel = createBoardPanel();
        JPanel actionsPanel = createActionsPanel();

        this.add(infoPanel, BorderLayout.NORTH);
        this.add(boardPanel, BorderLayout.CENTER);
        this.add(actionsPanel, BorderLayout.SOUTH);

        setupEventListeners();

        setVisible(true);
    }

    //metodos publicos

        //metedos para atualização de ui
    public void updateCell(int row, int col, Pokemon pokemon) {
        // Acessa o botão correto na nossa matriz de botões da UI
        JButton button = cellButtons[row][col];
        
        // Desabilita o botão para que não possa ser clicado novamente
        button.setEnabled(false);

        // Verifica se um Pokémon foi encontrado para decidir qual texto mostrar
        if (pokemon != null) {
            button.setText(pokemon.getName());
            // No futuro, você pode querer mostrar um ícone em vez de texto:
            // button.setIcon(...)
        } else {
            button.setText("-"); // Marca a célula como vazia
        }
    }

        //método da interface observador
    @Override
    public void update(GameState gameState) {
        // verifica se o jogo acabou antes de qualquer outra atualização
        if (gameState.isGameOver()) {
            checkEndGame(gameState);
            return;
        }

        // atualiza todos os componentes da ui com base no novo estado do jogo
        updateBoard(gameState);
        updateScoreLabels(gameState);
        updateHintButton(gameState);
        updateTurnIndicator(gameState); // idealmente, você teria um label para isso
    }


    //métodos privados

    /**
     * atualiza os labels de pontuação do jogador e do computador.
     */
    private void updateScoreLabels(GameState gameState) {
        playerScoreLabel.setText("Pontuação: " + gameState.getPlayer().getScore());
        computerScoreLabel.setText("Pontuação: " + gameState.getComputer().getScore());
    }

    /**
     * percorre todo o tabuleiro e atualiza o estado de cada botão (célula).
     */
    private void updateBoard(GameState gameState) {
        for (int row = 0; row < board.getSize(); row++) {
            for (int col = 0; col < board.getSize(); col++) {
                BoardCell cell = gameState.getBoard().getCellAt(row, col);
                JButton button = cellButtons[row][col];

                if (cell.isRevealed()) {
                    button.setEnabled(false);
                    Pokemon pokemon = cell.getPokemon();
                    if (pokemon != null) {
                        button.setText(pokemon.getName());
                        // futuramente, você pode adicionar um ícone aqui
                    } else {
                        button.setText("-"); // marca como vazio
                    }
                } else {
                    // garante que células não reveladas estejam no estado padrão
                    button.setEnabled(true);
                    button.setText("?");
                }
            }
        }
    }

    /**
     * atualiza o texto e o estado do botão de dica.
     */
    private void updateHintButton(GameState gameState) {
        int hints = gameState.getHintsRemaining();
        hintButton.setText("Dica (" + hints + ")");
        hintButton.setEnabled(hints > 0); // desabilita o botão se as dicas acabarem
    }

    /**
     * informa ao usuário de quem é o turno.
     * (requer um jlabel statuslabel no seu ui).
     */
    private void updateTurnIndicator(GameState gameState) {
        if (gameState.isPlayerTurn()) {
            // idealmente, um label de status mudaria de texto.
            // por enquanto, vamos usar o título da janela.
            setTitle("Pokémon - Campo de Batalha (Seu Turno)");
        } else {
            setTitle("Pokémon - Campo de Batalha (Turno do Computador)");
        }
    }

    /**
     * chamado quando a condição de fim de jogo é atingida.
     * abre a janela de fim de jogo e fecha a atual.
     */
    private void checkEndGame(GameState gameState) {
        // para evitar que a janela seja aberta múltiplas vezes
        this.dispose(); // fecha a janela do jogo principal
        
        // cria e exibe a janela de fim de jogo
        // você precisará criar o construtor de endgamewindow para receber o gamestate
        //new EndGameWindow(gameState); 
    }
    

        //metodos auxiliares do construtor
    private JPanel createInfoPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 10, 0));
        panel.setBorder(BorderFactory.createTitledBorder("Status dos Treinadores"));

        // Painel do Jogador
        JPanel playerPanel = new JPanel(new GridLayout(2, 1));
        playerNameLabel = new JLabel("Jogador: " + player.getName());
        playerScoreLabel = new JLabel("Pontuação: " + player.getScore());
        playerPanel.add(playerNameLabel);
        playerPanel.add(playerScoreLabel);

        // Painel do Computador
        JPanel computerPanel = new JPanel(new GridLayout(2, 1));
        computerNameLabel = new JLabel("Adversário: " + computer.getName());
        computerScoreLabel = new JLabel("Pontuação:" + computer.getScore());
        computerPanel.add(computerNameLabel);
        computerPanel.add(computerScoreLabel);

        panel.add(playerPanel);
        panel.add(computerPanel);
        return panel;
    }

    private JPanel createBoardPanel() {
        int boardSize = board.getSize();
        
        JPanel boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(boardSize, boardSize));
        
        JButton buttons[][] = new JButton[boardSize][boardSize];
        this.cellButtons = buttons;

        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                JButton button = new JButton("?"); // Começa "não revelado"
                BoardCell cell = board.getCellAt(row, col);

                if(cell.getRegion() == model.pokemon.PokemonType.ELECTRIC){
                    button.setBackground(new Color(209, 185, 14));
                }else if(cell.getRegion() == model.pokemon.PokemonType.GRASS){
                    button.setBackground(new Color(48, 185, 14));
                }else if(cell.getRegion() == model.pokemon.PokemonType.GROUND){
                    button.setBackground(new Color(103, 50, 9));
                }else if(cell.getRegion() == model.pokemon.PokemonType.WATER){
                    button.setBackground(new Color(25, 198, 210));
                }
                
                button.setOpaque(true);
                
                this.cellButtons[row][col] = button;
                boardPanel.add(button);
            }
        }
        return boardPanel;
    }

    private JPanel createActionsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Usei um painel extra para os botões do meio para melhor alinhamento
        JPanel centerButtonsPanel = new JPanel();
        swapPokemonButton = new JButton("Trocar Pokémon Principal");
        hintButton = new JButton("Dica (3)");
        
        centerButtonsPanel.add(swapPokemonButton);
        centerButtonsPanel.add(hintButton);

        exitButton = new JButton("Sair do Jogo");

        panel.add(centerButtonsPanel, BorderLayout.CENTER);
        panel.add(exitButton, BorderLayout.EAST);
        
        return panel;
    }

    private void setupEventListeners() {
        //configura listener para tabuleiro
        for (int row = 0; row < board.getSize(); row++) {
            for (int col = 0; col < board.getSize(); col++) {
                final int finalRow = row;
                final int finalCol = col;
                
                cellButtons[row][col].addActionListener(e -> {
                    gameController.handleCellClick(finalRow, finalCol);
                });
            }
        }

        //configura listener para botoes de ação
        exitButton.addActionListener(e -> {
            // Lógica para sair do jogo
            this.dispose();
            new WelcomeWindow();
        });
        
        // TODO: Implementar listeners para hintButton e swapPokemonButton
    }
}