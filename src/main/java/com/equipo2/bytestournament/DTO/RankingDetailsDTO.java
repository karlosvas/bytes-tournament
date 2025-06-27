package com.equipo2.bytestournament.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * RankingDetailsDTO es una clase que se utiliza para transportar datos de detalles de ranking
 * entre diferentes capas de una aplicación.
 * Su objetivo principal es encapsular y transferir información sobre el rendimiento de un jugador
 * en términos de victorias, derrotas, empates y puntos, sin exponer la
 * lógica interna ni las entidades del modelo de datos.
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
public class RankingDetailsDTO {
    private String username;
    private Integer wins;
    private Integer losses;
    private Integer draws;
    private Integer points;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n┌────────────────────────────┐\n");
        sb.append("│   Detalles del Ranking     │\n");
        sb.append("├─────────────┬──────────────┤\n");
        sb.append(String.format("│ %-11s │ %12s │\n", "Player", username));
        sb.append(String.format("│ %-11s │ %12d │\n", "Victorias", wins));
        sb.append(String.format("│ %-11s │ %12d │\n", "Derrotas", losses));
        sb.append(String.format("│ %-11s │ %12d │\n", "Empates", draws));
        sb.append(String.format("│ %-11s │ %12d │\n", "Puntos", points));
        sb.append("└─────────────┴──────────────┘\n");
        return sb.toString();
    }
}