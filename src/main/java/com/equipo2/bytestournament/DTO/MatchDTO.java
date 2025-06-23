package com.equipo2.bytestournament.DTO;

import com.equipo2.bytestournament.enums.Result;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;


/**
 *  MatchDTO es una clase que se utiliza para transportar datos entre diferentes capas de una aplicación,
 *  especialmente entre el backend y el frontend o entre servicios.
 * Su objetivo principal es encapsular y transferir solo la información necesaria, sin exponer la lógica interna
 * ni las entidades del modelo de datos.
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
@Schema(description = "DTO para partidas o encuentros")
public class MatchDTO {
    @Schema(description = "Identificador único del partido", example = "100", required = true)
    private Long id;

    @NotNull
    @Schema(description = "ID del torneo", example = "1", required = true)
    private Long tournament;

    @NotNull
    @Schema(description = "Jugador 1")
    private Long player1;

    @NotNull
    @Schema(description = "Jugador 2")
    private Long player2;

    @NotNull
    @Schema(description = "Resultado del partido")
    private Result result;

    @NotNull
    @Schema(description = "Número de ronda", example = "1")
    private Integer round;
}
