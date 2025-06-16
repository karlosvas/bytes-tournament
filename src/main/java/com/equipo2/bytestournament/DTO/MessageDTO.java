package com.equipo2.bytestournament.DTO;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
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

public class MessageDTO {

     @Schema(description = "Identificador único del mensaje", example = "1")
    private long id;
     
    @NotNull
    @Schema(description = "ID del remitente", example = "10", required = true)
    private long senderId;

    @NotEmpty
    @Schema(description = "Contenido del mensaje", example = "¡Hola!", required = true)
    private String content;

    @NotNull
    @Schema(description = "Fecha y hora del mensaje", example = "2024-06-16T15:30:00", required = true)
    private LocalDateTime timestamp;
    private long matchId;

    @NotNull
    @Schema(description = "ID del torneo relacionado", example = "200", required = true)
    
    private long tournamentId;

}
