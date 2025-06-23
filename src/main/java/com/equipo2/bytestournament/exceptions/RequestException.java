package com.equipo2.bytestournament.exceptions;

import java.util.Map;
import com.equipo2.bytestournament.enums.ApiResponse;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

/**
 * Excepción personalizada para representar errores de petición HTTP.
 * Esta clase extiende RuntimeException para permitir excepciones no verificadas
 * y proporciona información estructurada sobre los errores que ocurren durante
 * el procesamiento de las peticiones.
 * La anotación @Getter de Lombok genera automáticamente métodos getter para todos los campos.
 */
@Getter
public class RequestException extends RuntimeException {
    private final boolean hasError;
    private final String title;
    private final String detail;
    private final HttpStatus statusCode;
    private Map<String, String> reasons;
    private final ApiResponse apiResponse;
    private final Logger logger = LoggerFactory.getLogger(RequestException.class);

    /**
     * Constructor para crear una excepción de petición con un mensaje de error específico. mediante el enum ApiResponse.
     * {@link ApiResponse} Enum que representa el errores de la API
     */
    public RequestException(ApiResponse apiResponse) {
        this.hasError = true;
        this.title = apiResponse.getTitle();
        this.detail = apiResponse.getDetail();
        this.statusCode = apiResponse.getStatus();
        this.apiResponse = apiResponse;
        logger.error("ID error: {}", apiResponse);
    }

    /**
     * Constructor para crear una excepción de petición con un mensaje de error específico,
     * con un código de estado HTTP mendiante el enum apiResponse y algunos detalles adicionales
     * introducidos por el usuario.
     * @param apiResponse Enum que representa el errores de la API
     * @param title titulo de la excepcion
     * @param detail detalles de la excepcion
     */
    public RequestException(ApiResponse apiResponse, String title, String detail) {
        this.hasError = true;
        this.title = title;
        this.detail = detail;
        this.statusCode = apiResponse.getStatus();
        this.apiResponse = apiResponse;
        logger.error("ID error : {}", apiResponse);
    }
}