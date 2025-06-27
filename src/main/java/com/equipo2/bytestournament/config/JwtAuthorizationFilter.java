package com.equipo2.bytestournament.config;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import com.equipo2.bytestournament.utilities.Colours;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;

/**
 * JwtAuthorizationFilter es un filtro de autorización que se encarga de validar el token JWT
 * en cada solicitud HTTP. Extiende OncePerRequestFilter para asegurarse de que se ejecute una vez por solicitud.
 * Este filtro se llama justo depúes de ejecutarse el filtro de autenticación JWT.
 */
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    /**
     * jwtUtil es una utilidad para validar y extraer información de tokens JWT.
     * userDetailsService es un servicio que carga los detalles del usuario basado en el nombre de usuario.
     */
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    

    public JwtAuthorizationFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    /**
     * doFilterInternal se llama para procesar la solicitud HTTP.
     * Extrae el token JWT de la solicitud, lo valida y establece la autenticación en el contexto de seguridad
     * si el token es válido. Si el token no es válido o no se proporciona, no se establece ninguna autenticación.
     * @param request HttpServletRequest objeto que contiene la solicitud del cliente.
     * @param response HttpServletResponse objeto que contiene la respuesta al cliente.
     * @param filterChain FilterChain objeto que permite continuar con el procesamiento de la solicitud.
     * @return void
     * @throws ServletException si ocurre un error durante el procesamiento de la solicitud.
     * * @throws IOException si ocurre un error de entrada/salida durante el procesamiento de la solicitud.
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            // Extrae el JWT del encabezado de autorización de la solicitud
            // y valida el token.
            String jwt = extractJwtFromRequest(request);
            
            if (jwt != null && jwtUtil.validateToken(jwt)) {
                String username = jwtUtil.extractUsername(jwt);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                
                // Crea un objeto de autenticación con los detalles del usuario
                // y lo establece en el contexto de seguridad.
                UsernamePasswordAuthenticationToken authentication = 
                    new UsernamePasswordAuthenticationToken(
                        userDetails, 
                        null, 
                        userDetails.getAuthorities()
                );
                
                authentication.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
                );
                
                // Establece la autenticación en el contexto de seguridad
                // para que esté disponible en el resto de la aplicación.
                SecurityContextHolder.getContext().setAuthentication(authentication);

                logger.info(Colours.paintGreen("JWT válido para el usuario: " + username));
            }
        } catch (Error e) {
            throw new Error("Error al procesar el JWT", e);
        }
        
        // Continúa con la cadena de filtros para procesar la solicitud
        filterChain.doFilter(request, response);
    }
    
    // extractJwtFromRequest extrae el token JWT del encabezado de autorización de la solicitud HTTP.
    private String extractJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) 
            return bearerToken.substring(7);
            
        return null;
    }

   
}