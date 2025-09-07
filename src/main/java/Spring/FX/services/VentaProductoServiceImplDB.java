package Spring.FX.services;

import Spring.FX.domain.VentaProducto;
import Spring.FX.repositories.VentaProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VentaProductoServiceImplDB implements VentaProductoService {

    private final VentaProductoRepository ventaProductoRepository;

    @Autowired
    public VentaProductoServiceImplDB(VentaProductoRepository ventaProductoRepository) {
        this.ventaProductoRepository = ventaProductoRepository;
    }

    @Override
    public VentaProducto create(VentaProducto ventaProducto) {
        return ventaProductoRepository.save(ventaProducto);
    }

    @Override
    public List<VentaProducto> getAll() {
        return ventaProductoRepository.findAll();
    }

    @Override
    public List<VentaProducto> getByVentaId(Integer ventaId) {
        return ventaProductoRepository.findByVentaId(ventaId);
    }

    @Override
    public List<VentaProducto> getByProductoId(Integer productoId) {
        return ventaProductoRepository.findByProductoId(productoId);
    }

    @Override
    public VentaProducto update(Integer id, VentaProducto ventaProducto) {
        return ventaProductoRepository.findById(id).map(existente -> {
            existente.setVenta(ventaProducto.getVenta());
            existente.setProducto(ventaProducto.getProducto());
            existente.setCantidad(ventaProducto.getCantidad());
            return ventaProductoRepository.save(existente);
        }).orElseThrow(() -> new RuntimeException("VentaProducto no encontrado con id " + id));
    }

    @Override
    public void delete(Integer id) {
        ventaProductoRepository.deleteById(id);
    }
}
