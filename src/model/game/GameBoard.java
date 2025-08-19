package model.game;

import exception.InvalidPositionException;
import model.pokemon.Pokemon;
import model.pokemon.PokemonType;

import java.io.Serializable;

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

    //metodos publicos
    public void placePokemon(Pokemon pokemon, int row, int col) throws InvalidPositionException{
        BoardCell cell = getCellAt(row, col);
        
        if(cell.getRegion() != pokemon.getType()){
            throw new InvalidPositionException("Região invalida para tipo de Pokemon.");
        }

        if(cell.getPokemon() != null){
            throw new InvalidPositionException("Essa posição ja esta ocupada.");
        }
    
        cell.setPokemon(pokemon);
    }

    public int getSize()  { return this.size; }
    
    public BoardCell getCellAt(int row, int col) {
        if(row > size - 1 || col > size - 1){
            throw new IllegalArgumentException("Posição do grid inválida.");
        }
        return this.grid[row][col]; 
    }

    //metodos privados
    private void initCells(){
        int mid = size / 2;

        for(int row = 0; row < size; row++){
            for(int col = 0; col < size; col++){
                
                //determina qual a região da celula atual
                if(row < mid && col < mid){
                    this.grid[row][col] = new BoardCell(PokemonType.WATER);
                }else if(row < mid && col >= mid){
                    this.grid[row][col] = new BoardCell(PokemonType.GRASS);
                }else if(row >= mid && col < mid){
                    this.grid[row][col] = new BoardCell(PokemonType.GROUND);
                }else{
                    this.grid[row][col] = new BoardCell(PokemonType.ELECTRIC);
                }

            }
        }
    }
}