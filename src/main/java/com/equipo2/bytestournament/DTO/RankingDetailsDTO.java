package com.equipo2.bytestournament.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
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