package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Trainer implements Serializable {

    //atributos
    private String name;
    private List<Pokemon> team;
    private List<Pokemon> backpack; 
    private int score;
    private boolean isComputer;

    /**
     * @param name
     * @param isComputer
     */
    public Trainer(String name, boolean isComputer) {
        this.name = name;
        this.isComputer = isComputer;
        this.team = new ArrayList<>();
        this.backpack = new ArrayList<>();
        this.score = 0;
    }

    //metodos

    /**
     * adiciona inicial no time
     * @param pokemon a ser adicionado.
     */
    public void addInitialPokemon(Pokemon pokemon) {
        if (this.team.isEmpty()) {
            pokemon.setWild(false);
            this.team.add(pokemon);
            updateScore();
        }
    }

    /** 
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
