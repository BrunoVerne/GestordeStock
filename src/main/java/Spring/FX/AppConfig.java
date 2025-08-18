package Spring.FX;
import Spring.FX.controllers.*;
import Spring.FX.repositories.ProductoRepository;
import Spring.FX.repositories.UsuarioRepository;
import Spring.FX.services.ProductoService;
import Spring.FX.services.ProductoServiceImplDB;
import Spring.FX.services.UsuarioService;
import Spring.FX.services.UsuarioServiceImplDB;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public UsuarioService usuarioService(UsuarioRepository usuarioRepository) {
        return new UsuarioServiceImplDB(usuarioRepository);
    }

    @Bean
    public ProductoService productoService(ProductoRepository productoRepository) {
        return new ProductoServiceImplDB(productoRepository);
    }
}
