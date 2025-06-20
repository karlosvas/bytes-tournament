package com.equipo2.bytestournament.DTO;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.Map;

@Data
/**
 * ExceptionDTO es una clase que se utiliza para transportar datos de excepciones
 * entre diferentes capas de una aplicación.
 * Su objetivo principal es encapsular y transferir información sobre errores de manera estructurada,
 * sin exponer la lógica interna ni las entidades del modelo de datos.
 */
public class ExceptionDTO {
    private String title;
    private String detail;
    private int status;
    private Map<String, String> reasons;
    private LocalDateTime timestamp;

    public ExceptionDTO(String title, String detail, int status, Map<String, String> reasons, LocalDateTime timestamp) {
        this.title = title;
        this.detail = detail;
        this.status = status;
        this.reasons = reasons;
        this.timestamp = timestamp;
    }
}