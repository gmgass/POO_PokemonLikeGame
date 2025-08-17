package view;

import javax.swing.*;

import model.GameBoard;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;


public class WelcomeWindow extends JFrame{

    public WelcomeWindow(){
        //inicializa a janela
        super("Tela Inicial - Pokémon");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        //instancia containers
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        //cria componentes
        JLabel welcomeLabel = new JLabel("Selecione uma opção para continuar:");
        JButton newGame = new JButton("Distribuir time Pokémon.");
        JButton loadGame = new JButton("Carregar jogo salvo.");
        JButton randomGame =  new JButton("Distribuir aleatóriamente.");

        //add componentes nos containers
        buttonPanel.add(newGame);
        buttonPanel.add(loadGame);
        buttonPanel.add(randomGame);

        mainPanel.add(welcomeLabel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        this.add(mainPanel);

        setVisible(true);

        //implenta açoes para os botoes
        newGame.addActionListener( new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                //CHAMAR JANELA SETUPWINDOW
                model.GameBoard board = new GameBoard(8);
                ArrayList<model.Pokemon> list = new ArrayList<>();

                new SetupWindow(board, list);

                WelcomeWindow.this.dispose();

            }
        });

        loadGame.addActionListener( new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                //CARREGAR ARQUIVO DE JOGO PASSADO
            }
        });

        randomGame.addActionListener( new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                //GERAR DISTRIBUIÇÃO ALEATORIA DE POKEMONS NO TABULEIRO
            }
        });
    }
}