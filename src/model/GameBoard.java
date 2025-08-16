package model;

import java.io.Serializable;

/**
 * Representa o tabuleiro (mapa) do jogo.
 * Renomeei a classe para Tabuleiro para seguir o padrão em português que estávamos usando.
 */
public class GameBoard implements Serializable {

    private final int size;
    private final Pokemon[][] grid;

    public GameBoard(int size) {
        if (size % 2 != 0) {
            throw new IllegalArgumentException("O tabuleiro deve ter tamanho par");
        }

        this.size = size;
        this.grid = new Pokemon[size][size];
    }

    public int getTamanho() {
        return this.size;
    }
}