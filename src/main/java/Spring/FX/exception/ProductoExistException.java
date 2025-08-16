package Spring.FX.exception;

public class ProductoExistException extends RuntimeException {
  public ProductoExistException(String message) {
    super(message);
  }
}
