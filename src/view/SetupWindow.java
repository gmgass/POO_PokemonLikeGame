package view;

import java.awt.*;
import java.util.List;
import javax.swing.*;
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

        //CHAMAR MÉTODO setupEventListeners() QUE VAI CONFIGURAR OS EVENTOS DE TODOS OS BOTOES

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

                //DEFINIR A COR DE button COM BASE NA REGIAO DE cell
                
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
        this.startGameButton = debugButton;

        panel.add(status, BorderLayout.CENTER);
        panel.add(startButton, BorderLayout.EAST);
        panel.add(debugButton, BorderLayout.WEST);

        return panel;
    }

    private JPanel createPokemonListPanel( List<Pokemon> pokemonList ){
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        DefaultListModel<Pokemon> listModel = new DefaultListModel<Pokemon>();

        for (Pokemon pokemon : pokemonList) {
            listModel.addElement(pokemon);
        }

        this.pokemonListModel = listModel;
        JList<Pokemon> jList = new JList<Pokemon>(listModel);

        JScrollPane scrollPane = new JScrollPane(jList);

        panel.add(scrollPane);
        
        return panel;
    }
}
