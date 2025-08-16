package Spring.FX.services;

import Spring.FX.domain.Producto;
import Spring.FX.exception.ProductoNotFoundException;
import Spring.FX.repositories.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoServiceImplDB implements ProductoService{

    private final ProductoRepository productoRepository;

    public ProductoServiceImplDB(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Override
    public List<Producto> getAllProductos() {
        return productoRepository.findAll();
    }

    @Override
    public Producto findByNombre(String dato) {
        Optional<Producto> oEncontrado = productoRepository.findByNombre(dato);
        if(oEncontrado.isEmpty()){
            throw new RuntimeException("ese producto no existe");
        }
        return oEncontrado.get();

    }


    @Override
    public Producto createProducto(Producto producto) {
        Optional<Producto> oProducto = productoRepository.findById(producto. getCodigo());
        if(oProducto.isPresent()){
            throw new RuntimeException("no flaco el producto ya existe");
        }
        this.productoRepository.save(producto);
        return producto;
    }

    @Override
    public Producto actualizarProducto(Long id, Producto productoActualizado) {
        Producto productoExistente = productoRepository.findById(id)
                .orElseThrow(() -> new ProductoNotFoundException("Producto no encontrado con ID: " + id));

        // Actualiza solo los campos que vienen en productoActualizado
        if (productoActualizado.getNombre() != null) {
            productoExistente.setNombre(productoActualizado.getNombre());
        }
        if (productoActualizado.getPrecio() > 0) { // Validación básica
            productoExistente.setPrecio(productoActualizado.getPrecio());
        }
        if (productoActualizado.getCantidad() >= 0) { // Validación básica
            productoExistente.setCantidad(productoActualizado.getCantidad());
        }

        return productoRepository.save(productoExistente);
    }
}
