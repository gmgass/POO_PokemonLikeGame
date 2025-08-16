package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa um Treinador (jogador ou computador) no jogo.
 * Contém o time de Pokémons, a mochila e a pontuação.
 * Implementa Serializable para permitir salvar o jogo.
 */
public class Trainer implements Serializable {

    // ----- ATRIBUTOS -----
    private String name;
    private List<Pokemon> team;
    private List<Pokemon> backpack; // Mochila para armazenar Pokémons capturados 
    private int score;             // Pontuação do time [cite: 313]
    private boolean isComputer;

    // ----- CONSTRUTOR -----
    /**
     * Cria um novo treinador.
     * @param name O nome do treinador (ex: "Jogador" ou "Computador").
     * @param isComputer Define se o treinador é controlado pelo computador.
     */
    public Trainer(String name, boolean isComputer) {
        this.name = name;
        this.isComputer = isComputer;
        this.team = new ArrayList<>();
        this.backpack = new ArrayList<>();
        this.score = 0;
    }

    //  ================================================================================================
    //  --- MÉTODOS ---
    //  ================================================================================================

    /**
     * Adiciona um Pokémon inicial ao time do treinador.
     * Conforme o PDF, cada treinador começa com um Pokémon. 
     * @param pokemon O Pokémon a ser adicionado.
     */
    public void addInitialPokemon(Pokemon pokemon) {
        if (this.team.isEmpty()) {
            pokemon.setWild(false); // O Pokémon inicial pertence ao treinador
            this.team.add(pokemon);
            updateScore();
        }
    }

    /** 8============D
     * capturar um Pokémon selvagem. 
     * A lógica de captura é baseada em 50% de chance, como "lançar uma moeda". 
     * @param wildPokemon pokemom selvagem
     * @return true se capturado, false caso nao.
     */
    public boolean capturePokemon(Pokemon wildPokemon) {
        if (wildPokemon.isWild()) {
            if (Math.random() > 0.5) {
                System.out.println(this.name + " capturou " + wildPokemon.getName() + "!");
                wildPokemon.setWild(false); 

                // A lógica para adicioná-lo ao time principal deve implementada depois
                this.backpack.add(wildPokemon);
                updateScore();
                return true;
            } else {
                System.out.println(wildPokemon.getName() + " escapou!");
                //fugir para outro lugar -> classe que gerencia o tabuleiro.
                return false;
            }
        }
        return false;
    }
    //score
    public void updateScore() {
        int totalExp = 0;
        for (Pokemon p : this.team) {
            totalExp += p.getExp();
        }
        for (Pokemon p : this.backpack) {
            totalExp += p.getExp();
        }
        this.score = totalExp;
    }

//GETTERS E SETTERS
    public String getName() {
        return name;
    }

    public List<Pokemon> getTeam() {
        return team;
    }

    public List<Pokemon> getBackpack() {
        return backpack;
    }

    public int getScore() {
        return score;
    }

    public boolean isComputer() {
        return isComputer;
    }
}