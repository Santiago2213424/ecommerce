package ecomer.pe.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import ecomer.pe.service.UsuarioDetailsService;

@Configuration
public class SecurityConfig {

    @Autowired
    private UsuarioDetailsService usuarioDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // 🚀 RUTAS PÚBLICAS
                .requestMatchers("/", "/compras", "/login", "/registro",
                        "/css/**", "/js/**", "/img/**", "/uploads/**", "/redirect")
                    .permitAll()
                // 🔒 SOLO USUARIOS LOGUEADOS
                .requestMatchers("/carrito/**", "/checkout/**")
                    .authenticated()
                // 👑 SOLO ADMIN
                .requestMatchers("/admin/**", "/ventas/**", "/dashboard", "/usuarios/**")
                    .hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            // 🔐 LOGIN
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/redirect", true)
                // ⬇️ AQUI DISTINGUIMOS ENTRE CUENTA INACTIVA Y ERROR NORMAL
                .failureHandler((request, response, ex) -> {
                    if (ex instanceof DisabledException) {
                        response.sendRedirect("/login?inactivo=true");
                    } else {
                        response.sendRedirect("/login?error=true");
                    }
                })
                .permitAll()
            )
            // 🚪 LOGOUT
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/compras")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID")
            )
            // 🧿 SESIÓN
            .sessionManagement(session -> session
                .invalidSessionUrl("/login?expired=true")
                .sessionFixation().newSession()
            )
            .csrf(csrf -> csrf.disable())
            // Registrar nuestro provider
            .authenticationProvider(authProvider());

        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(usuarioDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
