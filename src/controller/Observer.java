package controller;

import model.game.GameState;

public interface Observer {
    void update(GameState gameState);
}
