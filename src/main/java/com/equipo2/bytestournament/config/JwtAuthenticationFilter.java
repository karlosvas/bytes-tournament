package com.equipo2.bytestournament.config;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
             // TODO: Deserializar directamente a tu modelo Usuario para ello es necesario el modelo
            // Usuario usuario = new ObjectMapper()
            // .readValue(request.getInputStream(), Usuario.class);


            // UsernamePasswordAuthenticationToken authToken = 
            //     new UsernamePasswordAuthenticationToken(
            //         loginRequest.getUsername(),
            //         loginRequest.getPassword()
            //     );

            // return authenticationManager.authenticate(authToken);
            return null;
        } catch (Error e) {
            throw new RuntimeException("Error al procesar las credenciales", e);
        }
    }
}