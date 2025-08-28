package Spring.FX.services;

import Spring.FX.domain.Producto;
import Spring.FX.domain.Usuario;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ProductoService {
    List<Producto> getAllProductos();
    Producto findById(Integer id);
    Producto findByNombre(String dato);
    Producto createProducto(Producto producto);
    Producto actualizarProducto(Integer id, Producto productoActualizado);
    Producto borrarProducto(Integer id);
    Optional<List<Producto>> findByUsuarioId(Integer usuarioId);

}
