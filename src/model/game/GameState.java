package model.game;

import exception.InvalidPositionException;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import model.pokemon.Pokemon;
import model.pokemon.PokemonType;

public class GameBoard implements Serializable {

    private final int size;
    private final BoardCell[][] grid;

    public GameBoard(int size) {
        if (size % 2 != 0) {
            throw new IllegalArgumentException("O tabuleiro deve ter tamanho par");
        }
        this.size = size;
        this.grid = new BoardCell[size][size];
        initCells();
    }

    public void distribuirPokemonsAleatoriamente(List<Pokemon> wildPokemons) {
        Collections.shuffle(wildPokemons);
        for (Pokemon p : wildPokemons) {
            boolean posicionado = false;
            while (!posicionado) {
                int linha = (int) (Math.random() * size);
                int coluna = (int) (Math.random() * size);
                BoardCell cell = getCellAt(linha, coluna);
                if (cell.getPokemon() == null && cell.getRegion() == p.getType()) {
                    cell.setPokemon(p);
                    posicionado = true;
                }
            }
        }
    }

    public void placePokemon(Pokemon pokemon, int row, int col) throws InvalidPositionException {
        BoardCell cell = getCellAt(row, col);
        if (cell.getRegion() != pokemon.getType()) {
            throw new InvalidPositionException("Região inválida para tipo de Pokemon.");
        }
        if (cell.getPokemon() != null) {
            throw new InvalidPositionException("Essa posição já está ocupada.");
        }
        cell.setPokemon(pokemon);
    }

    public int getSize() { return this.size; }
    
    public BoardCell getCellAt(int row, int col) {
        if (row >= size || col >= size || row < 0 || col < 0) {
            throw new IllegalArgumentException("Posição do grid inválida.");
        }
        return this.grid[row][col]; 
    }

    private void initCells() {
        int mid = size / 2;
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (row < mid && col < mid) {
                    this.grid[row][col] = new BoardCell(PokemonType.WATER);
                } else if (row < mid && col >= mid) {
                    this.grid[row][col] = new BoardCell(PokemonType.GRASS);
                } else if (row >= mid && col < mid) {
                    this.grid[row][col] = new BoardCell(PokemonType.GROUND);
                } else {
                    this.grid[row][col] = new BoardCell(PokemonType.ELECTRIC);
                }
            }
        }
    }
}