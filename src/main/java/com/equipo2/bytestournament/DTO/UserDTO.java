package com.equipo2.bytestournament.DTO;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.equipo2.bytestournament.enums.Rank;
import com.equipo2.bytestournament.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder.Default;

/**
 *  UserDTO es una clase que se utiliza para transportar datos entre diferentes capas de una aplicación,
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
@Schema(description = "DTO para usuarios")
public class UserDTO {
    @Schema(description = "Identificador único del usuario", example = "1", required = true)
    private Long id;

    @NotBlank
    @Schema(description = "Nombre de usuario", example = "usuario123")
    private String username;
    
    @NotBlank
    @Schema(description = "Correo electrónico", example = "usuario@gmail.com")
    private String email;

    @Default
    @NotBlank
    @Schema(description = "Contraseña del usuario", example = "password123")
    @Default
    private String password = "hidden"; // Se oculta la contraseña en la documentación

    @NotNull
    @Schema(description = "Rol del usuario", example = "ADMIN")
    private Role role;

    @Default
    @Schema(description = "Rango del usuario", example = "Oro")
    private Rank rank = Rank.BRONZE;

    @Default
    @Min(value = 0, message = "Los puntos deben ser al menos 0")
    @Schema(description = "Puntos del usuario", example = "1500")
    private Integer points = 0;

    @Default
    @Schema(description = "Lista de IDs de partidas en las que el usuario ha participado")
    @Default
    private List<Long> matches = new ArrayList<>();

    @Default
    @Schema(description = "Lista de IDs de torneos en los que el usuario ha participado")
    private List<Long> tournaments = new ArrayList<>();

    @Default
    @Schema(description = "Lista de privilegios de autoridad del usuario")
    private Set<String> authorityPrivilegies = new HashSet<>();
}