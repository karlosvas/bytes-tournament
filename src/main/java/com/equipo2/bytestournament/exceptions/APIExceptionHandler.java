package com.equipo2.bytestournament.exceptions;

import com.equipo2.bytestournament.DTO.ExceptionDTO;
import com.equipo2.bytestournament.config.JwtAuthenticationFilter;
import com.equipo2.bytestournament.enums.ApiResponse;
import jakarta.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Manejador centralizado de excepciones para la API.
 * Esta clase intercepta las excepciones lanzadas durante el procesamiento de las peticiones
 * y las convierte en respuestas HTTP estructuradas y consistentes.
 * @see RestControllerAdvice permite que los métodos de esta clase se apliquen globalmente a todos los controladores
 * de la aplicación.
 * @see ApiResponse para los códigos de error y mensajes utilizados en las respuestas.
 */
@RestControllerAdvice
public class APIExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(APIExceptionHandler.class);
    /**
     * Maneja excepciones de validación de argumentos de métodos.
     * Se activa cuando los datos de entrada no cumplen con las reglas de validación
     * especificadas por anotaciones como @Valid, @NotNull, etc.
     *
     * @param ex La excepción de validación capturada
     * @return ResponseEntity con detalles estructurados sobre los errores de validación
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionDTO> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach((error) -> errors.put(
                                                                            error.getField(),
                                                                            error.getDefaultMessage()));
        return new ResponseEntity<>(
                new ExceptionDTO(
                        ApiResponse.BAD_REQUEST.getTitle(),
                        ApiResponse.BAD_REQUEST.getDetail(),
                        ApiResponse.BAD_REQUEST.getStatus().value(),
                        errors,
                        ZonedDateTime.now().toLocalDateTime()),
                HttpStatus.BAD_REQUEST);
    }

    /**
     * Maneja excepciones específicas de la API como NullPointerException, IllegalArgumentException, etc.
     * Estas excepciones son lanzadas por la lógica de negocio
     * y pueden incluir errores de validación, errores de autenticación, etc.
     *
     * @param ex excepcion personalizada lanzada por la API
     * @return ResponseEntity con detalles estructurados sobre la excepción
     */
    @ExceptionHandler(RequestException.class)
    public ResponseEntity<ExceptionDTO> handleApiRequestException(RequestException ex) {
        ExceptionDTO apiException = new ExceptionDTO(
                ex.getTitle(),
                ex.getDetail(),
                ex.getStatusCode().value(),
                ex.getReasons(),
                ZonedDateTime.now(ZoneId.of("Z")).toLocalDateTime());

        return new ResponseEntity<>(apiException, ex.getStatusCode());
    }

    /**
     * Maneja excepciones de tipo NoHandlerFoundException.
     * Esta excepción se lanza cuando no se encuentra un controlador para la URL solicitada.
     *
     * @param ex excepcion lanzada por la API
     * @return ResponseEntity con detalles estructurados sobre la excepción
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionDTO> handleNotFoundException(NoHandlerFoundException ex) {
        return new ResponseEntity<>(
                new ExceptionDTO(
                        ApiResponse.ENDPOINT_NOT_FOUND.getTitle(),
                        ApiResponse.ENDPOINT_NOT_FOUND.getDetail(),
                        ApiResponse.ENDPOINT_NOT_FOUND.getStatus().value(),
                        null,
                        ZonedDateTime.now().toLocalDateTime()),
                ApiResponse.ENDPOINT_NOT_FOUND.getStatus());
    }

    /**
     * Maneja excepciones de tipo HttpMessageNotReadableException.
     * Esta excepción se lanza cuando el cuerpo de la solicitud no se puede leer o analizar correctamente.
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ExceptionDTO> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        String cause = ex.getMessage();
        String sol="";

        if(cause.contains("Enum"))
            sol = "The role is not accepted for the Enum";
        else if(cause.contains("Date"))
            sol = "Date format is not correct, the correct format is dd/MM/yyyy";
        else
            sol = "Error in the JSON format";

        ExceptionDTO apiException = new ExceptionDTO(
                "Error en el formato del JSON",
                sol,
                HttpStatus.BAD_REQUEST.value(),
                null,
                ZonedDateTime.now().toLocalDateTime()
        );
        return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);
    }

            /**
         * Maneja excepciones específicas de autorización cuando un usuario
         * intenta acceder a un recurso sin los permisos necesarios.
         * 
         * @param ex La excepción de autorización denegada
         * @return ResponseEntity con detalles sobre el error de autorización
         */
        @ExceptionHandler(AuthorizationDeniedException.class)
        public ResponseEntity<ExceptionDTO> handleAuthorizationDeniedException(AuthorizationDeniedException ex) {
            // Crear DTO de excepción utilizando ApiResponse predefinido
            ExceptionDTO exceptionDTO = new ExceptionDTO(
                    ApiResponse.AUTHENTICATION_FAILED.getTitle(),
                    "No tienes los permisos necesarios para realizar esta operación",
                    ApiResponse.AUTHENTICATION_FAILED.getStatus().value(),
                    null,
                    ZonedDateTime.now().toLocalDateTime());
            
            // Log del error
            logger.warn("Access denied para usuario: {}", 
                    SecurityContextHolder.getContext().getAuthentication() != null ? 
                    SecurityContextHolder.getContext().getAuthentication().getName() : "unknown");
                    
            // Devolver respuesta con estado 403 FORBIDDEN
            return new ResponseEntity<>(exceptionDTO, ApiResponse.AUTHENTICATION_FAILED.getStatus());
        }

    /**
     * Maneja excepciones generales que no son capturadas por otros manejadores.
     * Esta es una última línea de defensa para asegurar que todas las excepciones
     * sean manejadas y se devuelva una respuesta estructurada al cliente.
     *
     * @param ex excepcion lanzada por la API
     * @return ResponseEntity con detalles estructurados sobre la excepción
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionDTO> handleAllExceptions(Exception ex) {
        return new ResponseEntity<>(
                new ExceptionDTO(
                        ApiResponse.INTERNAL_SERVER_ERROR.getTitle(),
                        ApiResponse.INTERNAL_SERVER_ERROR.getDetail(),
                        ApiResponse.INTERNAL_SERVER_ERROR.getStatus().value(),
                        null,
                        ZonedDateTime.now().toLocalDateTime()),
                ApiResponse.INTERNAL_SERVER_ERROR.getStatus());
    }
}