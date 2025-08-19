package controller;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import model.game.GameState;

public class GamePersistence {

    /**
     * @param gameState 
     * @param filename
     */
    public void saveGame(GameState gameState, String filename) {

        try (FileOutputStream fileOut = new FileOutputStream(filename);
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            
            // Serializa GameState e escreve no arquivo.
            out.writeObject(gameState);
            
            System.out.println("Jogo salvo com sucesso em " + filename);

        } catch (IOException e) {
            System.out.println("Erro ao salvar o jogo: " + e.getMessage());
        }
    }

    /**
     * @param filename 
     * @return  GameState com todos os dados do jogo, ou null se der erro.
     */
    public GameState loadGame(String filename) {

        try (FileInputStream fileIn = new FileInputStream(filename);
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            
            // le o arquivo, desserializa os dados e recria o objeto GameState.
            GameState gameState = (GameState) in.readObject();
            
            System.out.println("Jogo carregado com sucesso de " + filename);
            return gameState;

        } catch (IOException e) {
            // erros de leitura
            System.out.println("Erro ao carregar o jogo: " + e.getMessage());
            return null;
        } catch (ClassNotFoundException e) {
            // arquivo de save ja incompativel.
            System.out.println("Erro: A classe do jogo salvo não foi encontrada.");
            return null;
        }
    }
}