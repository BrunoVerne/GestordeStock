package Spring.FX.exception;

public class UsuarioExistException extends RuntimeException {
    public UsuarioExistException(String message) {
        super(message);
    }
}
