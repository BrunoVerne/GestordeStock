package Spring.FX.services;

import Spring.FX.domain.Usuario;
import Spring.FX.exception.ProductoNotFoundException;
import Spring.FX.repositories.UsuarioRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
@Service
public class UsuarioServiceImplDB implements UsuarioService{

    private final UsuarioRepository usuarioRepository;

    public UsuarioServiceImplDB(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Usuario createUsuario(Usuario usuario) {
        Optional<Usuario> oUsuario = this.usuarioRepository.findByMail(usuario.getMail());
        if(oUsuario.isPresent()){
            throw new RuntimeException("ya existe este usuario");
        }
        this.usuarioRepository.save(usuario);
        return usuario;
    }

    @Override
    public Usuario modificarUsuario(int codigo, Usuario modificado) {
        Usuario usuarioExistente = usuarioRepository.findById(codigo)
                .orElseThrow(() -> new ProductoNotFoundException("Producto no encontrado con ID: " + codigo));
        usuarioExistente.setNombre(modificado.getNombre());
        usuarioExistente.setMail(modificado.getMail());
        usuarioExistente.setContrasenia(modificado.getContrasenia());
        return modificado;
    }

    @Override
    public Usuario borrarUsuario(int codigo) {
        Usuario usuarioExistente = usuarioRepository.findById(codigo)
                .orElseThrow(() -> new ProductoNotFoundException("Producto no encontrado con ID: " + codigo));
        this.usuarioRepository.delete(usuarioExistente);
        return usuarioExistente;
    }

    @Override
    public Boolean autenticar(String email, String password) throws Exception {
        Usuario usuario = usuarioRepository.findByMail(email)
                .orElseThrow(() -> new Exception("Credenciales inválidas"));

        if(!usuario.getContrasenia().equals(password)) {
            throw new Exception("Credenciales inválidas");
        }

        return true;
    }

    @Override
    public Optional<Usuario> findByMail(String email) {
        return Optional.ofNullable(usuarioRepository.findByMail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con email: " + email)));
    }
}
