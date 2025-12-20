package Spring.FX.services;

import Spring.FX.domain.Usuario;


import java.util.Optional;
public interface UsuarioService {
    Usuario createUsuario(Usuario usuario);
    Usuario modificarUsuario(int  codigo, Usuario usuarioModificado);
    Usuario borrarUsuario(int codigo);
    Usuario autenticar(String email, String password) throws Exception;
    Optional<Usuario> findByMail(String mail);

}
