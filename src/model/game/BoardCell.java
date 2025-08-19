package model.game;

import java.io.Serializable;

import model.pokemon.Pokemon;
import model.pokemon.PokemonType;

public class BoardCell implements Serializable{
    private final PokemonType region;
    private Pokemon pokemon;
    private boolean isRevealed;

    public BoardCell(PokemonType region){
        this.region = region;
        this.pokemon = null;
        this.isRevealed = false;
    }

    public PokemonType getRegion() {
        return region;
    }

    public Pokemon getPokemon() {
        return pokemon;
    }
    
    public boolean isRevealed() {
        return isRevealed;
    }

    public void setPokemon(Pokemon pokemon) {
        this.pokemon = pokemon;
    }

    public void setRevealed(boolean isRevealed) {
        this.isRevealed = isRevealed;
    }
}