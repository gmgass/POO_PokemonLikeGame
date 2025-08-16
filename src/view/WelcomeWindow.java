package view;

import javax.swing.*;
import java.awt.*;
import kava.awt.event.*;


public class WelcomeWindow extends JFrame{


    public WelcomeWindow(){
        //inicializa a janela
        super("Tela Inicial - Pokémon");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        //instancia containers
        JPanel mainPanel = new JPanel( new BoxLayout(mainPanel, BoxLayout.Y_AXIS) );
        JPanel buttonPanel = new JPanel( new BoxLayout(buttonPanel, BoxLayout.Y_AXIS) );

        //cria componentes
        JLabel welcomeLabel = new JLabel("Selecione uma opção para continuar:");
        JButton newGame = new JButton("Distribuir time Pokémon.");
        JButton loadGame = new JButton("Carregar jogo salvo.");
        JButton randomGame =  new JButton("Distribuir aleatóriamente.");

        //add componentes nos containers
        buttonPanel.add(newGame);
        buttonPanel.add(loadGame);
        buttonPanel.add(randomGame);

        mainPanel.add(welcomeLabel);
        mainPanel.add(buttonPanel);

        this.add(mainPanel);
    }
}