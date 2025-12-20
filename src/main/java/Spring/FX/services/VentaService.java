package Spring.FX.services;

import Spring.FX.domain.Venta;

import java.util.List;
import java.util.Optional;

public interface VentaService {
    Venta findById(Integer id);
    List<Venta> getAllVentas();
    Venta modificarVenta(Integer id, Venta ventaActualizada);
    Venta borrarVenta(Integer id);
    void createVenta(Venta venta);
    Optional<List<Venta>> findByUsuarioId(Integer usuarioId);


}
