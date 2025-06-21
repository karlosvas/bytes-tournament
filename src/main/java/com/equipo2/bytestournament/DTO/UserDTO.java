package com.equipo2.bytestournament.DTO;

import java.util.List;
import java.util.Set;
import com.equipo2.bytestournament.enums.Rank;
import com.equipo2.bytestournament.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO para usuarios")
@Builder
/**
 *  UserDTO es una clase que se utiliza para transportar datos entre diferentes capas de una aplicación,
 *  especialmente entre el backend y el frontend o entre servicios.
 *  Su objetivo principal es encapsular y transferir solo la información necesaria, sin exponer la lógica interna
 *  ni las entidades del modelo de datos.
 */
public class UserDTO {
    @Schema(description = "Identificador único del usuario", example = "1", required = true)
    private Long id;

    @NotBlank
    @Schema(description = "Nombre de usuario", example = "usuario123")
    private String username;
    
    @NotBlank
    @Schema(description = "Correo electrónico", example = "usuario@gmail.com")
    private String email;

    @NotBlank
    @Schema(description = "Contraseña del usuario", example = "password123")
    private String password;

    @NotNull
    @Schema(description = "Rol del usuario", example = "ADMIN")
    private Role role;

    @NotBlank
    @Schema(description = "Rango del usuario", example = "Oro")
    private Rank rank;

    @NotNull
    @Schema(description = "Puntos del usuario", example = "1500")
    private Integer points;

    @Schema(description = "Lista de IDs de partidas en las que el usuario ha participado")
    private List<Long> matches;

    @Schema(description = "Lista de IDs de torneos en los que el usuario ha participado")
    private List<Long> tournaments;

    @Schema(description = "Lista de privilegios de autoridad del usuario")
    private Set<String> authorityPrivilegies;
}