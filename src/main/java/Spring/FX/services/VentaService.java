package Spring.FX.services;

import Spring.FX.domain.Producto;
import Spring.FX.domain.Venta;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface VentaService {
    Venta findById(Integer id);
    List<Venta> getAllVentas();
    Venta modificarVenta(Integer id, Venta ventaActualizada);
    Venta borrarVenta(Integer id);
    Venta createVenta(Venta venta);
    Optional<List<Venta>> findByUsuarioId(Integer usuarioId);


}
