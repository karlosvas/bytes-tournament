package com.equipo2.bytestournament.DTO;

import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank; 
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *  MessageDTO es una clase que se utiliza para transportar datos entre diferentes capas de una aplicación,
 *  especialmente entre el backend y el frontend o entre servicios.
 *  Su objetivo principal es encapsular y transferir solo la información necesaria, sin exponer la lógica interna
 *  ni las entidades del modelo de datos.
 * 
 * {@link Data} Anotación de Lombok que genera automáticamente los métodos getter, setter, toString, equals y hashCode.}
 * {@link AllArgsConstructor} Anotación de Lombok que genera un constructor con todos los campos como parámetros.
 * {@link NoArgsConstructor} Anotación de Lombok que genera un constructor sin parámetros
 * {@link Builder} Anotación de Lombok que permite crear instancias de la clase utilizando el patrón Builder.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "DTO para mensajes en el sistema")
public class MessageDTO {

    @Schema(description = "Identificador único del mensaje", example = "1")
    private Long id;
     
    @NotNull
    @Schema(description = "ID del remitente", example = "10")
    private Long senderId;

    @NotBlank
    @Schema(description = "Contenido del mensaje", example = "¡Hola!")
    private String content;

    @Schema(description = "Fecha y hora del mensaje", example = "2024-06-16T15:30:00")
    private LocalDateTime timestamp;

    @NotNull
    @Schema(description = "ID de la partida si aplica", example = "300")
    private Long matchId;

    @NotNull
    @Schema(description = "ID del torneo relacionado", example = "200")
    private Long tournamentId;
}
