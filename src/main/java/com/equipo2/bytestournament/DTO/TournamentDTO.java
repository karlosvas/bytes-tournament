package com.equipo2.bytestournament.DTO;

import com.equipo2.bytestournament.enums.Status;

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
@Schema(description = "DTO para torneos")
/**
 *  TournamentDTO es una clase que se utiliza para transportar datos entre diferentes capas de una aplicación,
 *  especialmente entre el backend y el frontend o entre servicios.
 * Su objetivo principal es encapsular y transferir solo la información necesaria, sin exponer la lógica interna
 * ni las entidades del modelo de datos.
 */
public class TournamentDTO {

    @NotNull
    @Schema(description = "Identificador único del torneo", example = "1")
    private Long id;

    @NotBlank
    @Schema(description = "Nombre del torneo", example = "Torneo de Primavera")
    private String name;

    @NotNull
    @Schema(description = "Estado del torneo", example = "EN_CURSO")
    private Status status;
}
