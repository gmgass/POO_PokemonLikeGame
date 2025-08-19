// Local: src/view/BattleWindow.java
package view;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import model.battle.Battle;
import model.battle.BattleResult;
import model.pokemon.Pokemon;

/**
 * uma janela de diálogo modal que exibe a interface gráfica de uma batalha pokémon.
 * ela interage com a classe de lógica 'battle' para processar os rounds e retorna um 'battleresult'.
 */
public class BattleWindow extends JDialog {

    private final Battle battle;
    private BattleResult result;

    // componentes da ui
    private JLabel playerPokemonNameLabel;
    private JProgressBar playerHealthBar;
    private JLabel opponentPokemonNameLabel;
    private JProgressBar opponentHealthBar;
    private JTextArea battleLogArea;
    private JButton attackButton;
    private JButton fleeButton;

    /**
     * construtor que cria e exibe a janela de batalha.
     * @param owner o frame pai (a main gamewindow).
     * @param battle a instância da batalha contendo a lógica.
     */
    public BattleWindow(Frame owner, Battle battle) {
        super(owner, "Batalha Pokémon!", true); // 'true' para ser modal
        this.battle = battle;

        setSize(500, 400);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(10, 10, 10, 10));

        // inicializa os painéis
        JPanel pokemonsPanel = createPokemonsPanel();
        JScrollPane logScrollPane = createLogPanel();
        JPanel actionsPanel = createActionsPanel();

        add(pokemonsPanel, BorderLayout.NORTH);
        add(logScrollPane, BorderLayout.CENTER);
        add(actionsPanel, BorderLayout.SOUTH);

        setupEventListeners();
        updateUI(); // atualiza a ui com os dados iniciais da batalha
    }

    // --- métodos públicos ---

    /**
     * torna a janela visível e aguarda até que seja fechada, então retorna o resultado.
     * @return o resultado da batalha (vencedor/perdedor ou se houve fuga).
     */
    public BattleResult showBattle() {
        setVisible(true); // esta chamada irá bloquear até que a janela seja fechada
        return result;
    }

    // --- métodos privados para construção da ui ---

    private JPanel createPokemonsPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 1, 0, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Combatentes"));

        // painel do jogador
        JPanel playerPanel = new JPanel(new BorderLayout());
        playerPokemonNameLabel = new JLabel();
        playerHealthBar = new JProgressBar();
        playerPanel.add(playerPokemonNameLabel, BorderLayout.NORTH);
        playerPanel.add(playerHealthBar, BorderLayout.CENTER);

        // painel do oponente
        JPanel opponentPanel = new JPanel(new BorderLayout());
        opponentPokemonNameLabel = new JLabel();
        opponentHealthBar = new JProgressBar();
        opponentPanel.add(opponentPokemonNameLabel, BorderLayout.NORTH);
        opponentPanel.add(opponentHealthBar, BorderLayout.CENTER);
        
        panel.add(playerPanel);
        panel.add(opponentPanel);

        return panel;
    }

    private JScrollPane createLogPanel() {
        battleLogArea = new JTextArea();
        battleLogArea.setEditable(false);
        battleLogArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        battleLogArea.setText("A batalha começou!\n");
        return new JScrollPane(battleLogArea);
    }

    private JPanel createActionsPanel() {
        JPanel panel = new JPanel();
        attackButton = new JButton("Atacar");
        fleeButton = new JButton("Fugir");
        panel.add(attackButton);
        panel.add(fleeButton);
        return panel;
    }

    // --- métodos para lógica e eventos ---

    private void setupEventListeners() {
        attackButton.addActionListener(e -> executePlayerAttack());
        
        fleeButton.addActionListener(e -> {
            this.result = new BattleResult(); // cria um resultado de fuga
            dispose(); // fecha a janela
        });
    }

    private void executePlayerAttack() {
        // executa o round na lógica da batalha e pega o log
        String roundLog = battle.executeRound();
        appendLogMessage(roundLog);
        updateUI();

        // verifica se a batalha terminou
        if (battle.isBattleOver()) {
            endBattle();
            return;
        }

        // desabilita os botões para o turno do oponente
        setButtonsEnabled(false);
        
        // executa o turno do oponente após um pequeno atraso em outra thread
        // para não congelar a ui.
        new Timer(1000, e -> {
            ((Timer)e.getSource()).stop(); // executa apenas uma vez
            executeOpponentTurn();
        }).start();
    }

    private void executeOpponentTurn() {
        String roundLog = battle.executeRound();
        appendLogMessage(roundLog);
        updateUI();

        if (battle.isBattleOver()) {
            endBattle();
        } else {
            // reabilita os botões para o próximo turno do jogador
            setButtonsEnabled(true);
        }
    }
    
    private void endBattle() {
        this.result = battle.getResult();
        appendLogMessage("\nA batalha terminou! " + result.getWinner().getName() + " venceu!");
        setButtonsEnabled(false);
        
        // fecha a janela automaticamente após 2 segundos
        new Timer(2000, e -> {
            ((Timer)e.getSource()).stop();
            dispose();
        }).start();
    }
    
    private void updateUI() {
        Pokemon playerPokemon = battle.getPlayerPokemon();
        Pokemon opponentPokemon = battle.getOpponentPokemon();

        // atualiza nomes e vida
        playerPokemonNameLabel.setText(String.format("%s (Nível: %d)", playerPokemon.getName(), playerPokemon.getLevel()));
        opponentPokemonNameLabel.setText(String.format("%s (Nível: %d)", opponentPokemon.getName(), opponentPokemon.getLevel()));
        
        playerHealthBar.setMaximum(playerPokemon.getMaxHealth());
        playerHealthBar.setValue(playerPokemon.getHealth());
        playerHealthBar.setString(playerPokemon.getHealth() + " / " + playerPokemon.getMaxHealth());
        playerHealthBar.setStringPainted(true);

        opponentHealthBar.setMaximum(opponentPokemon.getMaxHealth());
        opponentHealthBar.setValue(opponentPokemon.getHealth());
        opponentHealthBar.setString(opponentPokemon.getHealth() + " / " + opponentPokemon.getMaxHealth());
        opponentHealthBar.setStringPainted(true);
    }

    private void appendLogMessage(String message) {
        battleLogArea.append(message + "\n");
        // faz o scroll para o final
        battleLogArea.setCaretPosition(battleLogArea.getDocument().getLength());
    }

    private void setButtonsEnabled(boolean enabled) {
        attackButton.setEnabled(enabled);
        fleeButton.setEnabled(enabled);
    }
}