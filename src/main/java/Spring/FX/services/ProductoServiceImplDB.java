package Spring.FX.services;

import Spring.FX.domain.Producto;
import Spring.FX.exception.ProductoExistException;
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
        List<Producto> productos = productoRepository.findAll();
        return productos;
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
    public void createProducto(Producto producto) {
        // Verificar que el producto tenga usuario asociado
        if (producto.getUsuario() == null || producto.getUsuario().getId() == null) {
            throw new RuntimeException("El producto debe tener un usuario asociado válido");
        }

        // Verificar si ya existe un producto con el mismo nombre para este usuario
        boolean productoExistente = productoRepository
                .existsByNombreAndUsuarioId(producto.getNombre(), producto.getUsuario().getId());

        if (productoExistente) {
            throw new ProductoExistException("Ya existe un producto con el nombre '" + producto.getNombre() + "' para este usuario");
        }

        // Asegurar que sea una creación (no actualización)
        producto.setId(null);
        productoRepository.save(producto);
    }

    @Override
    public void actualizarProducto(Integer id, Producto productoActualizado) {
        Producto productoExistente = productoRepository.findById(id)
                .orElseThrow(() -> new ProductoNotFoundException("Producto no encontrado con ID: " + id));
        productoExistente.setNombre(productoActualizado.getNombre());
        productoExistente.setPrecio(productoActualizado.getPrecio());
        productoExistente.setCantidad(productoActualizado.getCantidad());


        productoRepository.save(productoExistente);
    }

    @Override
    public Producto findById(Integer id){
        return productoRepository.findById(id)
                .orElseThrow(() -> new ProductoNotFoundException("Producto no encontrado con ID: " + id));
    }

    @Override
    public void borrarProducto(Integer id){
        Producto productoExistente = productoRepository.findById(id)
                .orElseThrow(() -> new ProductoNotFoundException("Producto no encontrado con ID: " + id));
        this.productoRepository.delete(productoExistente);
    }

    @Override
    public Optional<List<Producto>> findByUsuarioId(Integer usuarioId) {
        return productoRepository.findByUsuarioId(usuarioId);


    }




}
