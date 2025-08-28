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
        System.out.println("DEBUG: Obteniendo todos los productos");
        List<Producto> productos = productoRepository.findAll();
        System.out.println("DEBUG: Total de productos en BD: " + productos.size());
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
    public Producto createProducto(Producto producto) {
        // Verificar que el producto tenga usuario asociado
        if (producto.getUsuario() == null) {
            throw new RuntimeException("El producto debe tener un usuario asociado");
        }

        System.out.println("DEBUG: Creando producto - Nombre: " + producto.getNombre() + 
                          ", Precio: " + producto.getPrecio() + 
                          ", Cantidad: " + producto.getCantidad() + 
                          ", Usuario ID: " + producto.getUsuario().getCodigo());

        Optional<List<Producto>> oProducto = productoRepository.findByUsuarioId(producto.getUsuario().getCodigo());

        if (oProducto.isPresent() && !oProducto.get().isEmpty()) {
            throw new RuntimeException("Ya existe un producto con ese nombre para este usuario");
        }

        producto.setCodigo(null);
        System.out.println("DEBUG: Antes de guardar - Producto: " + producto);
        Producto productoGuardado = productoRepository.save(producto);
        System.out.println("DEBUG: Producto guardado con ID: " + productoGuardado.getCodigo());
        System.out.println("DEBUG: Producto guardado completo: " + productoGuardado);
        return productoGuardado;
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
        System.out.println("DEBUG: Buscando productos para usuario ID: " + usuarioId);
        Optional<List<Producto>> resultado = productoRepository.findByUsuarioId(usuarioId);
        if (resultado.isPresent()) {
            System.out.println("DEBUG: Encontrados " + resultado.get().size() + " productos");
            resultado.get().forEach(p -> System.out.println("DEBUG: Producto encontrado: " + p));
        } else {
            System.out.println("DEBUG: No se encontraron productos");
        }
        return resultado;
    }




}
