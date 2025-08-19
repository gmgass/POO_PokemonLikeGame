package model.battle;

import model.pokemon.Pokemon;

public class BattleResult {
    private final Pokemon winner;
    private final Pokemon loser;
    private final boolean wasFlee; // indica se a batalha terminou por fuga

    //construtor para um resultado de batalha com vencedor e perdedor.
    public BattleResult(Pokemon winner, Pokemon loser) {
        this.winner = winner;
        this.loser = loser;
        this.wasFlee = false;
    }

    //construtor para um resultado de batalha onde houve fuga.
    public BattleResult() {
        this.winner = null;
        this.loser = null;
        this.wasFlee = true;
    }

    // getters
    public Pokemon getWinner() { return winner; }
    public Pokemon getLoser() { return loser; }
    public boolean wasFlee() { return wasFlee; }
    
}
