package Spring.FX.services;

import Spring.FX.domain.Producto;
import Spring.FX.domain.Venta;

import java.util.List;
import java.util.Optional;

public interface VentaService {
    Venta getVenta();
    List<Venta> getVentas();
    Venta modificarVenta();
    Venta borrarVenta();
    Venta createVenta();
    Optional<List<Producto>> findByUsuarioId(Integer usuarioId);


}
