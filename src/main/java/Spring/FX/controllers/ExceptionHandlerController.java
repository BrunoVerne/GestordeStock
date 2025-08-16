package Spring.FX.controllers;

import Spring.FX.exception.ProductoExistException;
import Spring.FX.exception.ProductoNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(ProductoExistException.class)
    public ResponseEntity<?> productoExiste(ProductoExistException e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT) // 409 para conflictos
                .body(e.getMessage());
    }

    @ExceptionHandler(ProductoNotFoundException.class)
    public ResponseEntity<?> productoNoEncontrado(ProductoNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND) // 404 para no encontrado
                .body(e.getMessage());
    }
}