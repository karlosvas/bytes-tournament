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
@Schema(description = "DTO para partidas o encuentros")
/**
 *  MatchDTO es una clase que se utiliza para transportar datos entre diferentes capas de una aplicación,
 *  especialmente entre el backend y el frontend o entre servicios.
 * Su objetivo principal es encapsular y transferir solo la información necesaria, sin exponer la lógica interna
 * ni las entidades del modelo de datos.
 */
public class MatchDTO {
    @NotNull
    @Schema(description = "Identificador único del partido", example = "100")
    private Long id;

    @NotNull
    @Schema(description = "ID del torneo", example = "1")
    private Long torunamentId;

    @NotNull
    @Schema(description = "Jugador 1")
    private User player1;

    @NotNull
    @Schema(description = "Jugador 2")
    private User player2;

    @NotNull
    @Schema(description = "Resultado del partido")
    private Result result;

    @NotNull
    @Schema(description = "Número de ronda", example = "1")
    private Integer round;
}
