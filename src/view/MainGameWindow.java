package view;

import javax.swing.JButton;
import javax.swing.JLabel;

import model.*;

public class MainGameWindow {
    //atributos de jogo
    private GameBoard board;
    private Trainer player;
    private Trainer computer;

    //atributos de ui
    private JButton[][] cellButtons;
    private JLabel playerLabel;
    private JLabel cpuLabel;
    private JButton swapPokemonButton, hintButton, exitButton;

    public MainGameWindow(GameBoard board){
        this.board = board;
    }


}
