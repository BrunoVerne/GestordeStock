package Spring.FX.repositories;

import Spring.FX.domain.Producto;
import Spring.FX.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    Optional<Producto> findByNombre(String dato);

    @Query("SELECT p FROM Producto p WHERE p.usuario.id = :usuarioId")
    Optional<List<Producto>>  findByUsuarioId(@Param("usuarioId") Integer usuarioId);

    boolean existsByNombreAndUsuarioId(String nombre, Integer usuarioId);
}
