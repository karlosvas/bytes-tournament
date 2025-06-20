package com.equipo2.bytestournament.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.equipo2.bytestournament.service.CustomUserDetailsService;

/**
 * see @Configuración este bean define la configuración de seguridad de la aplicación.
 * see @EnableWebSecurity Habilita la seguridad web en de Spring.
 * see @EnableMethodSecurity Habilita la seguridad a nivel de método, permitiendo el uso de anotaciones como @PreAuthorize.
 * see @Bean Define beans necesarios para la configuración de seguridad.
 * Esta clase configura la seguridad de la aplicación, incluyendo la autenticación y autorización de usuarios.
 * Utiliza JWT para la autenticación y define un usuario en memoria con un nombre de usuario y contraseña.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);
    /**
     * Un PasswordEncoder es un componente que se utiliza para codificar contraseñas de forma segura.
     * 
     * @return PasswordEncoder que utiliza BCrypt.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * securityFilterChain define la cadena de filtros HTTP que intercepta cada petición.
     * 
     * @param http HttpSecurity para configurar la seguridad web.
     * @param jwtUtil utilidad para manejar JWT.
     * @param uds CustomUserDetailsService para cargar detalles del usuario.
     * @return SecurityFilterChain configurado con las reglas de seguridad.
     * @throws Exception si ocurre un error durante la configuración de seguridad.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtUtil jwtUtil, CustomUserDetailsService uds, AuthenticationManager authenticationManager) throws Exception {
        logger.info("Configurando la cadena de filtros de seguridad HTTP");
        // Desabilita CSRF, establece la política de sesión como sin estado (stateless) y configura las reglas de autorización
        http
        .csrf(csrf -> csrf.disable())
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(authorize -> authorize
            .requestMatchers("/api/admin/**").hasRole("ADMIN")
            .requestMatchers("/api/auth/**",
                            "/swagger-ui.html",
                            "/swagger-ui/**",
                            "/v3/api-docs/**").permitAll() // Cualquier operación en /auth es permitida sin autenticación y Swagger UI y OpenAPI también son accesibles sin autenticación
            .anyRequest().authenticated() 
        )
        // Se llama a el filtro de autenticación JWT y posteriormente al filtro de autorización JWT
        .addFilter(
            new JwtAuthenticationFilter(jwtUtil, authenticationManager))
        .addFilterBefore(
            new JwtAuthorizationFilter(jwtUtil, uds), 
            UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * inMemoryUserDetailsManager define un usuario en memoria para la autenticación al iniciar la aplicación.
     * Este usuario tiene un nombre de usuario "user", una contraseña "pass" y el rol "USER".
     * @return InMemoryUserDetailsManager con el usuario definido.
    */
    @Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager(){
        UserDetails user = User.withUsername("user")
                .password(passwordEncoder().encode("pass"))
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }

    /**
     * authProvider define un proveedor de autenticación que utiliza el servicio de detalles del usuario y el codificador de contraseñas combinando el servicio con el cifrador BCrypt.
     * 
     * @param uds CustomUserDetailsService para cargar detalles del usuario.
     * @return DaoAuthenticationProvider configurado con el servicio de detalles del usuario y el codificador de contraseñas.
     */
    @Bean
    public DaoAuthenticationProvider authProvider(CustomUserDetailsService uds) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(uds);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }
    
    /**
     * Expone el AuthenticationManager como un bean para ser utilizado en componentes que requieren autenticación.
     * Spring Security automáticamente registrará en este AuthenticationManager cualquier AuthenticationProvider
     * definido como bean en el contexto de la aplicación.
     * 
     * @param authConfig Configuración de autenticación proporcionada por Spring Security.
     * @return El AuthenticationManager configurado por Spring Security.
     * @throws Exception si ocurre un error durante la obtención del AuthenticationManager.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
