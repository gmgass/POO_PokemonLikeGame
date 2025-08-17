package view;

import java.awt.*;
import javax.swing.*;
import model.*;


public class SetupWindow extends JFrame {
    
    private final int WIDTH = 800;
    private final int HEIGHT = 600;

    private GameBoard board;
    private DefaultListModel<Pokemon> listModel; // Gemini disse que era melhor assim, preciso aprender sobre.

    public SetupWindow( GameBoard board, List<Pokemon> PokemonCompleteList ) {
        this.board = board;

        setTitle( "DEBUG Mode" );
        setSize( WIDTH, HEIGHT );
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        setLocationRelativeTo( null );
        setLayout( new BorderLayout( 10, 10 ) );
    }
}
