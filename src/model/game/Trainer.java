package model.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import model.pokemon.Pokemon;


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

    /**
     * Retorna o Pokémon principal, que é o primeiro (e único) da lista 'team'.
     * @return O Pokémon ativo para batalhas, ou null se o time estiver vazio.
     */
    public Pokemon getMainPokemon() {
        if (!team.isEmpty()) {
            return team.get(0);
        }
        return null;
    }
    
    /**
     * Verifica se o treinador tem Pokémons na mochila para poder trocar.
     * @return true se a mochila não estiver vazia.
     */
    public boolean hasPokemonInBackpack() {
        return !backpack.isEmpty();
    }
    
    /**
     * Troca o Pokémon principal. O antigo principal vai para a mochila
     * e o novo Pokémon selecionado vem da mochila para o time.
     * @param newMainPokemon O Pokémon da mochila que se tornará o novo principal.
     */
    public void swapMainPokemon(Pokemon newMainPokemon) {
        if (newMainPokemon == null || !backpack.contains(newMainPokemon)) {
            // Apenas uma verificação de segurança, não deve acontecer com a UI correta.
            return;
        }

        // 1. Guarda o Pokémon principal antigo
        Pokemon oldMainPokemon = getMainPokemon();

        // 2. Remove o novo Pokémon da mochila
        backpack.remove(newMainPokemon);

        // 3. Coloca o novo Pokémon no time
        team.set(0, newMainPokemon);
        
        // 4. Coloca o Pokémon antigo na mochila
        if (oldMainPokemon != null) {
            backpack.add(oldMainPokemon);
        }
        
        JOptionPane.showMessageDialog(null, this.name + " trocou " + oldMainPokemon.getName() + " por " + newMainPokemon.getName() + "!");
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
