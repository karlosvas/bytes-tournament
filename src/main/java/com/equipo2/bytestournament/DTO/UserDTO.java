package com.equipo2.bytestournament.DTO;

import com.equipo2.bytestournament.enums.Role;

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
@Schema(description = "DTO para usuarios")

/**
 *  UserDTO es una clase que se utiliza para transportar datos entre diferentes capas de una aplicación,
 *  especialmente entre el backend y el frontend o entre servicios.
 * Su objetivo principal es encapsular y transferir solo la información necesaria, sin exponer la lógica interna
 * ni las entidades del modelo de datos.
 */
public class UserDTO {

    @NotNull
    @Schema(description = "Identificador único del usuario", example = "1")
    private Long id;

    @NotBlank
    @Schema(description = "Nombre de usuario", example = "usuario123")
    private String username;

    @NotBlank
    @Schema(description = "Correo electrónico", example = "usuario@gmail.com")
    private String email;

    @NotBlank
    @Schema(description = "Contraseña", example = "password123")
    private String password;

    @NotNull
    @Schema(description = "Rol del usuario", example = "ADMIN")
    private Role role;

    @NotBlank
    @Schema(description = "Rango del usuario", example = "Oro")
    private String rank;

    @NotNull
    @Schema(description = "Puntos del usuario", example = "1500")
    private Integer points;
}