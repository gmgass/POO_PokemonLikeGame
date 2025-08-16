package model;

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
    }

    public int getTamanho() {
        return this.size;
    }
}