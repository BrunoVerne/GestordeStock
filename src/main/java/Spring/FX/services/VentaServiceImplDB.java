package Spring.FX.services;

import Spring.FX.domain.Venta;
import Spring.FX.repositories.VentaRepository;

import java.util.List;
import java.util.Optional;
import Spring.FX.exception.VentaNotFounException;
import org.springframework.stereotype.Service;

@Service
public class VentaServiceImplDB implements VentaService{
    private final VentaRepository ventaRepository;

    public VentaServiceImplDB(VentaRepository ventaRepository) {
        this.ventaRepository = ventaRepository;
    }


    @Override
    public Venta findById(Integer id) {
        return ventaRepository.findById(id)
                .orElseThrow(() -> new VentaNotFounException("venta no encontrada"));
    }

    @Override
    public List<Venta> getAllVentas() {
        return ventaRepository.findAll();
    }

    @Override
    public Venta modificarVenta(Integer id, Venta ventaActualizada) {
        return null;
    }


    @Override
    public Venta borrarVenta(Integer id) {
         Venta existente = ventaRepository.findById(id)
                 .orElseThrow(() -> new VentaNotFounException("Venta no encontrada"));
        this.ventaRepository.delete(existente);
        return existente;
    }

    @Override
    public void createVenta(Venta venta) {
        if (venta.getUsuario() == null) {
            throw new RuntimeException("El producto debe tener un usuario asociado");
        }
        venta.setId(null);
        ventaRepository.save(venta);
    }

    @Override
    public Optional<List<Venta>> findByUsuarioId(Integer usuarioId) {
        return ventaRepository.findByUsuarioId(usuarioId);


    }
}
