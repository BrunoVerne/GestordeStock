package Spring.FX.services;

import Spring.FX.domain.Usuario;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public interface UsuarioService {
    Usuario createUsuario(Usuario usuario);
    Usuario modificarUsuario(int  codigo, Usuario usuarioModificado);
    Usuario borrarUsuario(int codigo);
    Boolean autenticar(String email, String password) throws Exception;
    Optional<Usuario> findByMail(String mail);

}
