package view;

import java.awt.*;
import java.util.List;
import javax.swing.*;

import exception.InvalidPositionException;
import model.*;


public class SetupWindow extends JFrame {
    
    private final GameBoard board;
    private Pokemon selectedPokemon;
    private DefaultListModel<Pokemon> pokemonListModel;

    //atributos de interface 
    private JButton[][] cellButtons;
    private JList<Pokemon> pokemonJList;
    private JLabel statusLabel;
    private JButton startGameButton;
    private JButton debugButton;

    //construtor
    public SetupWindow(GameBoard board, List<Pokemon> pokemonCompleteList){
        this.board = board;

        setTitle("Modo de Preparação");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10,10));
        
        JPanel pokemonList = createPokemonListPanel(pokemonCompleteList);
        JPanel boardPanel = createBoardPanel();
        JPanel statusPanel = createStatusPanel();

        this.add(boardPanel, BorderLayout.CENTER);
        this.add(pokemonList, BorderLayout.EAST);
        this.add(statusPanel, BorderLayout.SOUTH);

        setupEventListeners();

        setVisible(true);
    }

    //métodos privados

        //metodos auxiliares do construtor
    private JPanel createBoardPanel(){
        int boardSize = board.getSize();
        
        JPanel boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(boardSize, boardSize));
        
        JButton buttons[][] = new JButton[boardSize][boardSize];
        this.cellButtons = buttons;

        for(int row = 0; row < boardSize; row++){
            for(int col = 0; col < boardSize; col++){
                JButton button = new JButton();
                BoardCell cell = board.getCellAt(row, col);

                if(cell.getRegion() == PokemonType.ELECTRIC){
                    button.setBackground(new Color(209, 185, 14));
                }else if(cell.getRegion() == PokemonType.GRASS){
                    button.setBackground(new Color(48, 185, 14));
                }else if(cell.getRegion() == PokemonType.GROUND){
                    button.setBackground(new Color(103, 50, 9));
                }else if(cell.getRegion() == PokemonType.WATER){
                    button.setBackground(new Color(25, 198, 210));
                }

                button.setOpaque(true);
                
                this.cellButtons[row][col] = button;
                boardPanel.add(button); 
            }
        }
    
        return boardPanel;
    }

    private JPanel createStatusPanel(){
        JPanel panel = new JPanel(new BorderLayout());
        
        JLabel status = new JLabel("Selecione um Pokémon da lista e clique no tabuleiro.");
        this.statusLabel = status;
        
        JButton startButton = new JButton("Iniciar Jogo");
        this.startGameButton = startButton;

        JButton debugButton = new JButton("DEBUG MODE");
        this.debugButton = debugButton;

        panel.add(status, BorderLayout.CENTER);
        panel.add(startButton, BorderLayout.EAST);
        panel.add(debugButton, BorderLayout.WEST);

        return panel;
    }

    private JPanel createPokemonListPanel( List<Pokemon> pokemonList ){
        JPanel panel = new JPanel(new BorderLayout());

        DefaultListModel<Pokemon> listModel = new DefaultListModel<Pokemon>();

        for (Pokemon pokemon : pokemonList) {
            listModel.addElement(pokemon);
        }
        this.pokemonListModel = listModel;
        
        JList<Pokemon> jList = new JList<Pokemon>(listModel);
        this.pokemonJList = jList;

        JScrollPane scrollPane = new JScrollPane(jList);

        panel.add(scrollPane);
        
        return panel;
    }

    private void setupEventListeners(){
        
        //configura listener para a lista de pokemons
        pokemonJList.addListSelectionListener(e -> {
            if(!e.getValueIsAdjusting()){       //evita ser disparado 2x(uma quando pressiona o botao e uma quando solta)
                selectedPokemon = pokemonJList.getSelectedValue();

                if(this.selectedPokemon != null){
                    statusLabel.setText("Selecionado:" + this.selectedPokemon.getName());
                }
            }
        });
    
        //configura listener para tabuleiro
        for(int row = 0; row < board.getSize(); row++){
            for(int col = 0; col < board.getSize(); col++){
                
                //gemini mando, tenho q entender
                final int finalRow = row;
                final int finalCol = col;

                JButton button = this.cellButtons[row][col];

                button.addActionListener(e -> {
                    if(this.selectedPokemon == null){
                        statusLabel.setText("ERRO: Selecione um Pokemon antes de jogar");
                        return;
                    }

                    try {
                        board.placePokemon(selectedPokemon, finalRow, finalCol);

                        button.setText(selectedPokemon.getName());
                        button.setEnabled(false);

                        pokemonListModel.removeElement(selectedPokemon);
                        
                        this.selectedPokemon = null;
                        pokemonJList.clearSelection();

                        statusLabel.setText("Selecione um Pokémon da lista e clique no tabuleiro.");

                    } catch (InvalidPositionException ex) {
                        statusLabel.setText("ERRO:" + ex.getMessage());
                    }
                });
            }
        }

        //configura listener para botoes start e debug

        startGameButton.addActionListener(e -> {
            new MainGameWindow(this.board);
            this.dispose();
        });

        debugButton.addActionListener(e -> {
            //TODO: permitir que a lista mostre todos os pokemons e nao só os do jogador
        });
    }
}
