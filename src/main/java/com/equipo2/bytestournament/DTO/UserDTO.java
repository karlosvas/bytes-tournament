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
@Schema(description = "DTO para usuarios")

public class UserDTO {

    @Schema(description = "Identificador único del usuario", example = "1")
    private long id;

    @NotEmpty
    @Schema(description = "Nombre de usuario", example = "usuario123", required = true)
    private String username;

    @NotEmpty
    @Schema(description = "Correo electrónico", example = "usuario@gmail.com", required = true)
    private String email;

    @NotEmpty
    @Schema(description = "Contraseña", example = "password123", required = true)
    private String password;

    @NotNull
    @Schema(description = "Rol del usuario", example = "ADMIN", required = true)
    private Role role;

    @NotEmpty
    @Schema(description = "Rango del usuario", example = "Oro", required = true)
    private String rank;

    @NotNull
    @Schema(description = "Puntos del usuario", example = "1500", required = true)
    private int points;
}