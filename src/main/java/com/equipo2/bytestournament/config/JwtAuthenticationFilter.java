package com.equipo2.bytestournament.config;

import java.io.IOException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JwtAuthenticationFilter es un filtro de autenticación que se encarga de procesar las solicitudes de inicio de sesión.
 * Extiende UsernamePasswordAuthenticationFilter para manejar la autenticación basada en nombre de usuario y contraseña.
 * Este filtro intercepta las solicitudes de autenticación, verifica las credenciales del usuario,
 * genera un token JWT si la autenticación es exitosa y maneja los errores de autenticación.
 */
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    /**
     * authenticationManager es el administrador de autenticación que se utiliza para autenticar las credenciales del usuario.
     * jwtUtil es una utilidad para generar y validar tokens JWT.
     */
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    public JwtAuthenticationFilter(JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    /**
     * attemptAuthentication se llama cuando se intenta autenticar un usuario.
     * Deserializa el cuerpo de la solicitud HTTP para obtener las credenciales del usuario
     * y crea un objeto UsernamePasswordAuthenticationToken.
     * @param request HttpServletRequest objeto que contiene la solicitud del cliente.
     * @param response HttpServletResponse objeto que contiene la respuesta al cliente.
     * @return Authentication objeto que representa la autenticación del usuario.
     * @throws AuthenticationException si ocurre un error durante el proceso de autenticación.
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
             // TODO: Deserializar directamente a tu modelo Usuario para ello es necesario el modelo Usuario.
            Usuario usuario = new ObjectMapper()
            .readValue(request.getInputStream(), Usuario.class);

            logger.info("Intento de autenticación para usuario: " + usuario.getUsername());
            UsernamePasswordAuthenticationToken authToken = 
                new UsernamePasswordAuthenticationToken(
                    usuario.getUsername(),
                    usuario.getPassword()
                );

            return authenticationManager.authenticate(authToken);
        } catch (Error e) {
            throw new RuntimeException("Error al procesar las credenciales", e);
        }
    }

    /**
     * successfulAuthentication se llama cuando la autenticación es exitosa.
     * Genera un token JWT y lo agrega a la respuesta HTTP.
     * @param request HttpServletRequest objeto que contiene la solicitud del cliente.
     * @param response HttpServletResponse objeto que contiene la respuesta al cliente.
     * @param chain FilterChain objeto que permite continuar con la cadena de filtros.
     * @param authResult Authentication objeto que contiene los detalles de la autenticación exitosa.
     * @throws IOException si ocurre un error de entrada/salida al procesar la solicitud o respuesta.
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        String token = jwtUtil.generateToken(authResult);
        logger.info("Token generado para: " + authResult.getName());
        response.addHeader("Authorization", "Bearer " + token);
    }

    /**
     * unsuccessfulAuthentication se llama cuando la autenticación falla.
     * Configura la respuesta HTTP con un código de estado 401 y un mensaje de error en formato JSON.
     * @param request HttpServletRequest objeto que contiene la solicitud del cliente.
     * @param response HttpServletResponse objeto que contiene la respuesta al cliente.
     * @param failed AuthenticationException objeto que contiene detalles sobre la falla de autenticación.
     * @throws IOException si ocurre un error de entrada/salida al procesar la solicitud o respuesta.
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        // Establecer código de estado 401 (Unauthorized), configurar tipo de contenido para la respuesta.
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        // Escribir respuesta de error en formato JSON
        response.getWriter().write("{\"error\": \"" + "Autenticación fallida: " + failed.getMessage() + "\"}");
        // Log de error
        logger.error("Falló la autenticación para la solicitud: " + request.getRequestURI(), failed);
    }
}