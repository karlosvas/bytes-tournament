package com.equipo2.bytestournament.DTO;

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
@Schema(description = "DTO para torneos")
public class TournamentDTO {

     @Schema(description = "Identificador único del torneo", example = "1")
    private long id;

     @NotEmpty
    @Schema(description = "Nombre del torneo", example = "Torneo de Primavera", required = true)
    private String name;

    @NotNull
    @Schema(description = "Estado del torneo", example = "EN_CURSO", required = true)
    private Status status;
}
