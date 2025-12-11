package ecomer.pe.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import ecomer.pe.model.Rol;
import ecomer.pe.model.Usuario;
import ecomer.pe.repository.UsuarioRepository;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(UsuarioRepository repo, BCryptPasswordEncoder encoder) {
        return args -> {

            if (repo.count() == 0) {

                // ----- ADMIN -----
                Usuario admin = new Usuario();
                admin.setNombre("Administrador");
                admin.setApellido("Principal");
                admin.setCorreo("admin@gmail.com");   // 🔥 ahora se usa para login
                admin.setUsername("admin");           // opcional
                admin.setPassword(encoder.encode("admin123"));
                admin.setRol(Rol.ADMIN);
                admin.setActivo(true);

                // ----- USER -----
                Usuario user = new Usuario();
                user.setNombre("Juan");
                user.setApellido("Pérez");
                user.setCorreo("usuario@gmail.com"); // 🔥 login con correo
                user.setUsername("usuario");
                user.setPassword(encoder.encode("user123"));
                user.setRol(Rol.USER);
                user.setActivo(true);

                repo.save(admin);
                repo.save(user);

                System.out.println("✔ Usuarios creados correctamente con correo como login.");
            }
        };
    }
}
