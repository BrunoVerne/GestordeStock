package Spring.FX.services;

import Spring.FX.domain.Producto;
import Spring.FX.domain.Venta;
import Spring.FX.repositories.VentaRepository;

import java.util.List;
import java.util.Optional;

public class VentaServiceImplDB implements VentaService{
    private final VentaRepository ventaRepository;

    public VentaServiceImplDB(VentaRepository ventaRepository) {
        this.ventaRepository = ventaRepository;
    }




    @Override
    public Venta getVenta() {
        return null;
    }

    @Override
    public List<Venta> getVentas() {
        return List.of();
    }

    @Override
    public Venta modificarVenta() {
        return null;
    }

    @Override
    public Venta borrarVenta() {
        return null;
    }

    @Override
    public Venta createVenta() {
        return null;
    }

    @Override
    public Optional<List<Producto>> findByUsuarioId(Integer usuarioId) {
        return Optional.empty();
    }
}
