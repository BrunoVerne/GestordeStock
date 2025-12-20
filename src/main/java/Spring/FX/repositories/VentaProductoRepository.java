package Spring.FX.repositories;

import Spring.FX.domain.VentaProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VentaProductoRepository extends JpaRepository<VentaProducto, Integer> {

    List<VentaProducto> findByVentaId(Integer ventaId);


}
