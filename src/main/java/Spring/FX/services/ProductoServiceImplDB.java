package Spring.FX.services;

import Spring.FX.domain.Producto;
import Spring.FX.domain.Usuario;
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
        Optional<Producto> oProducto = productoRepository.findByNombre(producto.getNombre());
        if(oProducto.isPresent()){
            throw new RuntimeException("no flaco el producto ya existe");
        }
        producto.setCodigo(null);
        this.productoRepository.save(producto);
        return producto;
    }

    @Override
    public Producto actualizarProducto(Integer id, Producto productoActualizado) {
        Producto productoExistente = productoRepository.findById(id)
                .orElseThrow(() -> new ProductoNotFoundException("Producto no encontrado con ID: " + id));
        productoExistente.setNombre(productoActualizado.getNombre());
        productoExistente.setPrecio(productoActualizado.getPrecio());
        productoExistente.setCantidad(productoActualizado.getCantidad());


        return productoRepository.save(productoExistente);
    }

    @Override
    public Producto findById(Integer id){
        return productoRepository.findById(id)
                .orElseThrow(() -> new ProductoNotFoundException("Producto no encontrado con ID: " + id));
    }

    @Override
    public Producto borrarProducto(Integer id){
        Producto productoExistente = productoRepository.findById(id)
                .orElseThrow(() -> new ProductoNotFoundException("Producto no encontrado con ID: " + id));
        this.productoRepository.delete(productoExistente);
        return productoExistente;
    }

    @Override
    public Optional<List<Producto>> findByUsuarioId(Integer usuarioId) {
        return productoRepository.findByUsuarioId(usuarioId);
    }


}
