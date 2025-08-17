// Local: src/App.java
import javax.swing.SwingUtilities;
import view.WelcomeWindow;
// Importe aqui outras classes que você possa precisar no futuro
// import model.*; 

/**
 * Classe principal que inicia a aplicação do jogo Pokémon.
 */
public class App {

    /**
     * O método main é o ponto de entrada de qualquer aplicação Java.
     * @param args Argumentos de linha de comando (não usaremos neste projeto).
     */
    public static void main(String[] args) {
        
        // Garante que a interface gráfica seja criada e atualizada na Thread de Despacho de Eventos (EDT).
        // Esta é a forma profissional e segura de iniciar uma aplicação Swing.
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Dentro deste método run, nós criamos a nossa primeira janela.
                // Isso instancia e, como definimos no construtor, torna a WelcomeWindow visível.
                new WelcomeWindow();
            }
        });
    }
}