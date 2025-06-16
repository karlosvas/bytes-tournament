package com.equipo2.bytestournament.DTO;

import javax.naming.spi.DirStateFactory.Result;

import org.apache.catalina.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO para partidos o encuentros")

public class MatchDTO {
    @Schema(description = "Identificador único del partido", example = "100")
    private long id;

    @NotNull
    @Schema(description = "ID del torneo", example = "1", required = true)
    private long torunamentId;

    @NotNull
    @Schema(description = "Jugador 1", required = true)
    private User player1;

    @NotNull
    @Schema(description = "Jugador 2", required = true)
    private User player2;

    @NotNull
    @Schema(description = "Resultado del partido", required = true)
    private Result result;

    @NotNull
    @Schema(description = "Número de ronda", example = "1", required = true)
    private int round;
}
