package com.equipo2.bytestournament.DTO;

import com.equipo2.bytestournament.enums.Status;
import com.equipo2.bytestournament.model.Match;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder.Default;
import java.util.ArrayList;
import java.util.List;

/**
 *  TournamentDTO es una clase que se utiliza para transportar datos entre diferentes capas de una aplicación,
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
@Schema(description = "DTO para torneos")
public class TournamentDTO {
    @Schema(description = "Identificador único del torneo", example = "1", required = true)
    private Long id;

    @NotBlank
    @Schema(description = "Nombre del torneo", example = "Torneo de Primavera")
    private String name;

    @NotNull
    @Default
    @Min(value = 2, message = "El número maxmimo de jugadores debe ser al menos 2")
    @Schema(description = "Cantidad máxima de participantes", example = "6", required = true)
    private Integer maxPlayers = 2;

    @NotNull
    @Schema(description = "Estado del torneo", example = "EN_CURSO")
    private Status status;

    @Default
    @NotNull
    @Min(value = 0, message = "El número de rondas debe ser al menos 0")
    @Schema(description = "Número de rondas del torneo", example = "3")
    private Integer rounds = 0;

    @NotNull
    @Default
    @Min(value = 1, message = "El número máximo de rondas debe ser al menos 1")
    @Schema(description = "Número máximo de rondas del torneo", example = "5")
    private Integer maxRounds = 1;

    @Default
    @Schema(description = "Lista de partidos asociados al torneo")
   
    private List<MatchDTO> matches = new ArrayList<>();
    @Schema(description = "Lista de jugadores que participan en el torneo")
    @Default
    private List<Long> players = new ArrayList<>();
}
