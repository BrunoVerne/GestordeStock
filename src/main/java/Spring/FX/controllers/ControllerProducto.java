package Spring.FX.controllers;

import Spring.FX.domain.Producto;
import Spring.FX.services.ProductoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/productos")
public class ControllerProducto {

    private final ProductoService productoService;

    public ControllerProducto(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public ResponseEntity<?> getAllProductos() {
        return ResponseEntity.ok(this.productoService.getAllProductos());
    }

    @GetMapping("/{nombre}")
    public Producto buscarPorNombre(@PathVariable String nombre) {
        return this.productoService.findByNombre(nombre);

    }

    @PostMapping
    public ResponseEntity<?> createProducto(@RequestBody Producto producto) {
        Producto response = this.productoService.createProducto(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response); // 201 CREATED para POST
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Producto> actualizarProducto(@PathVariable Long id, @RequestBody Producto actualizacion) {
        Producto productoActualizado = productoService.actualizarProducto(id, actualizacion);
        return ResponseEntity.ok(productoActualizado);
    }
}