package com.equipo2.bytestournament.config;

import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * @Component Esta clase es un componente de Spring que proporciona utilidades para manejar JSON Web Tokens (JWT).
 * @Value Anotaciones que permiten inyectar valores de propiedades desde el archivo de configuración.
 * 
 * JwtUtil es una clase que proporciona métodos para generar, validar y extraer información de tokens JWT.
 * Utiliza la biblioteca JJWT para manejar la creación y validación de tokens.
 */
@Component
public class JwtUtil {
    @Value("${jwt.secret}") private String secret;
    @Value("${jwt.expiration}") private long expiration;

    /**
     * generateToken genera un token JWT para un usuario autenticado.
     * Este token incluye el nombre de usuario, la fecha de emisión y la fecha de expiración.
     * 
     * @param auth Authentication objeto que contiene la información del usuario autenticado.
     * @return String token JWT generado.
     */
    public String generateToken(Authentication auth) {
        String username = auth.getName();
        Date now = new Date();
        Date exp = new Date(now.getTime() + expiration * expiration);

        return Jwts.builder()
                .setSubject(username) // Establece el sujeto del token como el nombre de usuario
                .setIssuedAt(now) // Establece la fecha de emisión del token.
                .setExpiration(exp) // Establece la fecha de expiración del token.
                .signWith(SignatureAlgorithm.HS512, secret) // Firma el token con el algoritmo HS512 y la clave secreta.
                .compact(); // Genera una representacion en texto del token separado en tres bloques codificados en Base64.
    }

    /**
     * validateToken valida un token JWT.
     * Este método intenta analizar el token utilizando la clave secreta.
     * Si el token es válido, devuelve true; de lo contrario, devuelve false.
     * 
     * @param token String token JWT a validar.
     * @return boolean true si el token es válido, false en caso contrario.
     */
    public boolean validateToken(String token) {
       try {
            // Intenta analizar el token con la clave secreta con la clave y analiza JWT.
            Jwts.parser()
            .setSigningKey(secret)
            .parseClaimsJws(token); 
            return true;
       } catch (Exception e) {
            return false;
       }
    }

    /**
     * extractUsername extrae el nombre de usuario del token JWT.
     * Este método analiza el token y obtiene el sujeto (nombre de usuario) del mismo.
     * 
     * @param token String token JWT del cual se extraerá el nombre de usuario.
     * @return String nombre de usuario extraído del token.
     */
    public String extractUsername(String token) {
        // Intenta analizar el token con la clave secreta con la clave y analiza JWT.
        // Si el token es válido, obtiene el cuerpo del token y extrae el sujeto (nombre de usuario o que es el subject en generateToken).
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
