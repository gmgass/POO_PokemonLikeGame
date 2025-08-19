package view;

import controller.GamePersistence;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;
import model.game.GameBoard;
import model.game.GameState;
import model.game.Trainer;
import model.pokemon.*;

/**
 * A primeira janela do jogo, que oferece ao jogador as opções iniciais para
 * começar uma nova partida.
 */
public class WelcomeWindow extends JFrame {

    // --- Atributos da Classe para os componentes da UI ---
    private JButton debugModeButton;
    private JButton loadGameButton;
    private JButton newGameButton;

    public WelcomeWindow() {
        // --- 1. Configuração da Janela Principal ---
        super("Bem-vindo ao Pokémon!");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centraliza na tela
        setLayout(new BorderLayout(10, 10));

        // --- 2. Criação e Configuração dos Componentes ---
        setupUI();

        // --- 3. Configuração dos Listeners de Eventos ---
        setupListeners();

        // --- 4. Torna a janela visível ---
        setVisible(true);
    }

    /**
     * Inicializa e organiza os componentes da interface gráfica.
     */
    private void setupUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        // Adiciona uma margem interna à janela
        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 10));

        // Título
        JLabel welcomeLabel = new JLabel("Welcome to our Pokemon-like game!\n", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        mainPanel.add(welcomeLabel, BorderLayout.NORTH);

        // Painel para os botões, com BoxLayout para empilhá-los verticalmente
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        // Instancia os botões usando os atributos da classe
        newGameButton = new JButton("New Game");
        loadGameButton = new JButton("Load Game");
        debugModeButton = new JButton("Debug mode");

        // Define o tamanho preferencial e alinhamento para os botões
        Dimension buttonSize = new Dimension(250, 35);
        Component[] buttons = { newGameButton, loadGameButton, debugModeButton };
        for (Component btn : buttons) {
            ((JButton) btn).setPreferredSize(buttonSize);
            ((JButton) btn).setMaximumSize(buttonSize);
            ((JButton) btn).setAlignmentX(Component.CENTER_ALIGNMENT);
            ((JButton) btn).setFont(new Font("Arial", Font.PLAIN, 16));
        }

        // Adiciona os botões ao painel com espaçamento entre eles
        buttonPanel.add(Box.createVerticalGlue()); // Espaço flexível acima para centralizar
        buttonPanel.add(newGameButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 8))); // Espaçamento fixo
        buttonPanel.add(loadGameButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        buttonPanel.add(debugModeButton);
        buttonPanel.add(Box.createVerticalGlue()); // Espaço flexível abaixo para centralizar

        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        this.add(mainPanel);
    }

    /**
     * Configura as ações que serão executadas quando os botões forem clicados.
     */
    private void setupListeners() {
        // Ação para o botão "Posicionar Time" que chama a SetupWindow
        debugModeButton.addActionListener(e -> {
            GameBoard board = new GameBoard(8);
            Trainer player = new Trainer("Jogador", false);
            Trainer computer = new Trainer("Computador", true);
            ArrayList<Pokemon> list = createCompletePokemonList();
            Random rand = new Random();

            if (!list.isEmpty()) {
                player.addInitialPokemon(list.remove(rand.nextInt(list.size())));
            }
            if (!list.isEmpty()) {
                computer.addInitialPokemon(list.remove(rand.nextInt(list.size())));
            }

            GameState gameState = new GameState(player, computer, board);
            new SetupWindow(gameState, list);
            this.dispose();
        });

        // Ação para o botão "Carregar Jogo" com JFileChooser
        loadGameButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser("."); // Começa no diretório do projeto
            fileChooser.setDialogTitle("Selecione o jogo salvo para carregar");
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Save files (*.sav)", "sav"));

            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                File fileToLoad = fileChooser.getSelectedFile();
                GameState loadedState = new GamePersistence().loadGame(fileToLoad.getAbsolutePath());

                if (loadedState != null) {
                    new MainGameWindow(loadedState);
                    this.dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Falha ao carregar o jogo.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Ação para o botão "Distribuir Aleatoriamente"
        newGameButton.addActionListener(e -> {
            GameBoard board = new GameBoard(8);
            Trainer player = new Trainer("Jogador", false);
            Trainer computer = new Trainer("Computador", true);
            ArrayList<Pokemon> list = createCompletePokemonList();
            Random rand = new Random();

            if (!list.isEmpty()) {
                player.addInitialPokemon(list.remove(rand.nextInt(list.size())));
            }
            if (!list.isEmpty()) {
                computer.addInitialPokemon(list.remove(rand.nextInt(list.size())));
            }

            // O método distribuirPokemonsAleatoriamente precisa ser adicionado ao seu GameBoard
            // board.distribuirPokemonsAleatoriamente(list); 

            GameState gameState = new GameState(player, computer, board);
            new MainGameWindow(gameState);
            this.dispose();
        });
    }

    /**
     * Cria e retorna uma lista completa de Pokémons para o início do jogo.
     * @return Uma ArrayList contendo os Pokémons.
     */
    private ArrayList<Pokemon> createCompletePokemonList() {
        ArrayList<Pokemon> list = new ArrayList<>();
        list.add(new PokemonGround("Steelix", 100, 10));
        list.add(new PokemonGround("Diglet", 100, 10));
        list.add(new PokemonGrass("Bulbasaur", 100, 10));
        list.add(new PokemonGrass("Oddish", 100, 10));
        list.add(new PokemonWater("Squirtle", 100, 10));
        list.add(new PokemonWater("Magikarp", 100, 10));
        list.add(new PokemonElectric("Pikachu", 100, 10));
        list.add(new PokemonElectric("Voltorb", 100, 10));
        return list;
    }
}