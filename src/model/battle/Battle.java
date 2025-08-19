// local: src/model/battle/Battle.java
package model.battle;

import model.pokemon.Pokemon;

/**
 * gerencia a lógica de uma batalha pokemon, round a round.
 * a classe é responsável por controlar turnos, ataques e determinar o vencedor.
 */
public class Battle {

    private final Pokemon playerPokemon;
    private final Pokemon opponentPokemon;
    private Pokemon attacker;
    private int turnCounter; // contador de turnos global para a batalha

    /**
     * construtor da batalha.
     * @param playerPokemon o pokemon do jogador que inicia a batalha.
     * @param opponentPokemon o pokemon adversário.
     */
    public Battle(Pokemon playerPokemon, Pokemon opponentPokemon) {
        this.playerPokemon = playerPokemon;
        this.opponentPokemon = opponentPokemon;
        this.attacker = playerPokemon; // o jogador que inicia a batalha sempre ataca primeiro
        this.turnCounter = 1;
    }

    /**
     * executa um único round de batalha.
     * @return uma string descrevendo a ação do round.
     */
    public String executeRound() {
        Pokemon target = (attacker == playerPokemon) ? opponentPokemon : playerPokemon;
        
        // armazena a vida antes do ataque para o log
        int healthBefore = target.getHealth();
        
        // executa o ataque
        attacker.attack(target);
        
        // calcula o dano causado
        int damageDealt = healthBefore - target.getHealth();
        
        String logMessage = attacker.getName() + " atacou " + target.getName() + ", causando " + damageDealt + " de dano!";
        
        // troca o atacante para o próximo round
        switchAttacker();
        this.turnCounter++;
        
        return logMessage;
    }
    
    /**
     * verifica se a batalha terminou (se algum pokemon foi derrotado).
     * @return true se a batalha acabou, false caso contrário.
     */
    public boolean isBattleOver() {
        return playerPokemon.getHealth() <= 0 || opponentPokemon.getHealth() <= 0;
    }

    /**
     * determina o resultado da batalha e restaura a energia dos pokemons.
     * @return um objeto BattleResult com o vencedor e o perdedor.
     */
    public BattleResult getResult() {
        Pokemon winner = (playerPokemon.getHealth() > 0) ? playerPokemon : opponentPokemon;
        Pokemon loser = (winner == playerPokemon) ? opponentPokemon : playerPokemon;
        
        // distribui experiencia para o vencedor
        // a quantidade de exp pode ser baseada no nível do perdedor
        int expGained = 20 + (loser.getLevel() * 5);
        winner.increaseExpPoints(expGained);

        // restaura a energia de ambos, conforme especificado no pdf
        playerPokemon.restoreHealth();
        opponentPokemon.restoreHealth();
        
        return new BattleResult(winner, loser);
    }

    /**
     * alterna o pokemon que atacará no próximo turno.
     */
    private void switchAttacker() {
        this.attacker = (this.attacker == playerPokemon) ? opponentPokemon : playerPokemon;
    }
    
    // getters para a ui (battlewindow) poder exibir as informações
    public Pokemon getPlayerPokemon() {
        return playerPokemon;
    }

    public Pokemon getOpponentPokemon() {
        return opponentPokemon;
    }

    public int getTurnCounter() {
        return turnCounter;
    }
}