package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import model.*;


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
        JButton newGame = new JButton("Novo jogo.");
        JButton loadGame = new JButton("Carregar jogo.");
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
                
                list.add(new PokemonGround("Steelix", 100, 10));
                list.add(new PokemonElectric("Pikachu", 100, 10));
                list.add(new PokemonWater("Squirtle", 100, 10));
                list.add(new PokemonGrass("Bulbasaur", 100, 10));

                new SetupWindow(board, list);

                WelcomeWindow.this.dispose();

            }
        });

        loadGame.addActionListener( e -> {
            // 1. Cria uma instância da sua classe de persistência
            GamePersistence persistence = new GamePersistence();
            
            // 2. Chama o método para carregar o jogo. 
            // Este método irá abrir a janela de seleção de arquivo para o usuário.
            GameState loadedState = persistence.loadGame("pokemon_save.sav"); // Usa um nome de arquivo padrão por enquanto

            // 3. Verifica se o jogo foi carregado com sucesso
            if (loadedState != null) {
                // 4. Se sim, inicia a janela principal do jogo com os dados carregados
                new MainGameWindow(loadedState);
                
                // 5. Fecha a janela de boas-vindas
                WelcomeWindow.this.dispose();
            } else {
                // Opcional: Mostra uma mensagem de erro se o carregamento falhar
                JOptionPane.showMessageDialog(WelcomeWindow.this, "Falha ao carregar o jogo.", "Erro", JOptionPane.ERROR_MESSAGE);
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