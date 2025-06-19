package com.equipo2.bytestournament.DTO;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank; 
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO para mensajes en el sistema")
/**
 *  MessageDTO es una clase que se utiliza para transportar datos entre diferentes capas de una aplicación,
 *  especialmente entre el backend y el frontend o entre servicios.
 *  Su objetivo principal es encapsular y transferir solo la información necesaria, sin exponer la lógica interna
 *  ni las entidades del modelo de datos.
 */
public class MessageDTO {
    @NotNull
    @Schema(description = "Identificador único del mensaje", example = "1")
    private Long id;
     
    @NotNull
    @Schema(description = "ID del remitente", example = "10")
    private Long senderId;

    @NotBlank
    @Schema(description = "Contenido del mensaje", example = "¡Hola!")
    private String content;

    @NotNull
    @Schema(description = "Fecha y hora del mensaje", example = "2024-06-16T15:30:00")
    private LocalDateTime timestamp;

    private Long matchId;

    @NotNull
    @Schema(description = "ID del torneo relacionado", example = "200")

    // TODO: Poner relación entre entidaddes
    private Long tournamentId;
}
