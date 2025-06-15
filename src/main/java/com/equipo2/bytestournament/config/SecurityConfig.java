package com.equipo2.bytestournament.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
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
 * @Configuración este bean define la configuración de seguridad de la aplicación.
 * @EnableWebSecurity Habilita la seguridad web en de Spring.
 * @EnableMethodSecurity Habilita la seguridad a nivel de método, permitiendo el uso de anotaciones como @PreAuthorize.
 * @Bean Define beans necesarios para la configuración de seguridad.
 * 
 * Esta clase configura la seguridad de la aplicación, incluyendo la autenticación y autorización de usuarios.
 * Utiliza JWT para la autenticación y define un usuario en memoria con un nombre de usuario y contraseña.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

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
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtUtil jwtUtil, CustomUserDetailsService uds) throws Exception {
        // Especificar que el filtro solo aplica a login
        JwtAuthenticationFilter jwtAuthFilter = new JwtAuthenticationFilter(jwtUtil, authenticationManager(http, uds));
        jwtAuthFilter.setFilterProcessesUrl("/auth/login");

        // Desabilita CSRF, establece la política de sesión como sin estado (stateless) y configura las reglas de autorización
        http
        .csrf(csrf -> csrf.disable())
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(authorize -> authorize
            .requestMatchers("/admin/**").hasRole("ADMIN")
            .requestMatchers("/auth/**").permitAll() // Cualquier operación en /auth es permitida sin autenticación
            .anyRequest().authenticated() 
        )
        // Se llama a el filtro de autenticación JWT y posteriormente al filtro de autorización JWT
        .addFilter(
            new JwtAuthenticationFilter(jwtUtil, authenticationManager(http, uds)))
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
     * authenticationManager define el administrador de autenticación que utiliza el proveedor de autenticación, e integra el provider en la cadena de la configuración de seguridad.
     * Este método es necesario para que Spring Security pueda manejar la autenticación de usuarios.
     * 
     * @param http HttpSecurity para configurar la seguridad web.
     * @param uds CustomUserDetailsService para cargar detalles del usuario.
     * @return AuthenticationManager configurado con el proveedor de autenticación.
     * @throws Exception si ocurre un error durante la configuración de autenticación.
     */
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, CustomUserDetailsService uds) throws Exception {
       return http.getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(authProvider(uds))
                .build();
    }
}
