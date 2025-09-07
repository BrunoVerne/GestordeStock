package Spring.FX.services;

import Spring.FX.domain.VentaProducto;

import java.util.List;

public interface VentaProductoService {

    VentaProducto create(VentaProducto ventaProducto);

    List<VentaProducto> getAll();

    List<VentaProducto> getByVentaId(Integer ventaId);

    List<VentaProducto> getByProductoId(Integer productoId);

    VentaProducto update(Integer id, VentaProducto ventaProducto);

    void delete(Integer id);
}
