package view;

import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import model.game.GameState;
import model.game.Trainer;

public class EndGameWindow extends JDialog {

    /**
     * Construtor que cria e exibe a janela de fim de jogo.
     * @param owner O frame pai (a MainGameWindow).
     * @param gameState O estado final do jogo para determinar o vencedor.
     */
    public EndGameWindow(MainGameWindow owner, GameState gameState) {
        super(owner, "Fim de Jogo", true); // Janela modal

        // --- 1. Determina o Vencedor ---
        Trainer player = gameState.getPlayer();
        Trainer computer = gameState.getComputer();
        String winnerMessage;

        if (player.getScore() > computer.getScore()) {
            winnerMessage = "Parabéns, " + player.getName() + "! Você venceu!";
        } else if (computer.getScore() > player.getScore()) {
            winnerMessage = "O " + computer.getName() + " venceu a partida!";
        } else {
            winnerMessage = "A partida terminou em empate!";
        }

        // --- 2. Configuração da Janela ---
        setSize(400, 200);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));

        // --- 3. Criação dos Componentes ---
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Mensagem do vencedor
        JLabel winnerLabel = new JLabel(winnerMessage, SwingConstants.CENTER);
        winnerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(winnerLabel, BorderLayout.CENTER);

        // Botão de Novo Jogo
        JButton newGameButton = new JButton("Novo Jogo");
        panel.add(newGameButton, BorderLayout.SOUTH);

        // --- 4. Configuração do Listener ---
        newGameButton.addActionListener(e -> {
            dispose(); // Fecha a janela de fim de jogo
            owner.dispose(); // Fecha a janela principal do jogo
            new WelcomeWindow(); // Abre a janela de boas-vindas
        });
        
        // --- 5. Adiciona o painel e torna a janela visível ---
        add(panel);
        setVisible(true);
    }
}