package Spring.FX.repositories;

import Spring.FX.domain.Producto;
import Spring.FX.domain.Usuario;
import Spring.FX.domain.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface VentaRepository extends JpaRepository<Venta, Integer> {
    @Query("SELECT v FROM Venta v WHERE v.usuario.id = :usuarioId")
    Optional<List<Venta>> findByUsuarioId(@Param("usuarioId") Integer usuarioId);
}
