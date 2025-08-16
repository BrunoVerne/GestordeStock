package Spring.FX.services;

import Spring.FX.domain.Producto;

import java.util.List;

public interface ProductoService {
    List<Producto> getAllProductos();
    Producto findByNombre(String dato);
    Producto createProducto(Producto producto);
    Producto actualizarProducto(Long id, Producto productoActualizado);



}
