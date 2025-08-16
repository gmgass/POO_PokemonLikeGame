package exception;

/**
 * Exceção lançada quando há uma tentativa de acessar
 * uma posição indisponível.
 */
public class InvalidPositionException extends Exception {
    /**
     * Construtor que recebe a mensagem de erro.
     * @param message Explica a causa da exceção lançada.
     */
    public InvalidPositionException( String message ) {
        super( message );
    }
}