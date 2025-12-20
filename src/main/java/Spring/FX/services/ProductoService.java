package Spring.FX.services;

import Spring.FX.domain.Producto;

import java.util.List;
import java.util.Optional;

public interface ProductoService {
    List<Producto> getAllProductos();
    Producto findById(Integer id);
    Producto findByNombre(String dato);
    void createProducto(Producto producto);
    void actualizarProducto(Integer id, Producto productoActualizado);
    void borrarProducto(Integer id);
    Optional<List<Producto>> findByUsuarioId(Integer usuarioId);

}
