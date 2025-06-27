package com.equipo2.bytestournament.DTO;

import java.util.ArrayList;
import java.util.List;
import com.equipo2.bytestournament.enums.Rank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder.Default;

/**
 *  RankingDTO es una clase que se utiliza para transportar datos de clasificación
 * entre diferentes capas de una aplicación.
 * Su objetivo principal es encapsular y transferir información sobre la clasificación de jugadores
 * de manera estructurada,
 * sin exponer la lógica interna ni las entidades del modelo de datos.
 * 
 * {@link Data} Anotación de Lombok que genera automáticamente los métodos getter, setter, toString, equals y hashCode.}
 * {@link AllArgsConstructor} Anotación de Lombok que genera un constructor con todos los campos como parámetros.
 * {@link NoArgsConstructor} Anotación de Lombok que genera un constructor sin parámetros
 * {@link Builder} Anotación de Lombok que permite crear instancias de la clase utilizando el patrón Builder.
 * {@link Default} Anotación de Lombok que inicializa la lista de jugadores como una lista vacía por defecto.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RankingDTO {

    public static class InnerClassificationDTO {
        public String playerName;
        public Rank rank;
        public Integer points;

        public InnerClassificationDTO(String playerName, Rank rank, Integer points) {
            this.playerName = playerName;
            this.points = points;
            this.rank = rank;
        }

        @Override
        public String toString() {
            return "\nJugador/a: " + this.playerName +  "\nRango: " + this.rank + "\nPuntos: " + this.points+"\n";
        }
    }

    @Default
    public List<InnerClassificationDTO> players = new ArrayList<>();

   @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n┌────┬────────────────────┬────────────┬──────────┐\n");
        sb.append("│    │ Jugador/a          │ Rango      │ Puntos   │\n");
        sb.append("├────┼────────────────────┼────────────┼──────────┤\n");
        int pos = 1;
        for (InnerClassificationDTO player : players) {
            sb.append(String.format("│ %-2d │ %-18s │ %-10s │ %-8d │\n",
                    pos++, player.playerName, player.rank, player.points));
        }
        sb.append("└────┴────────────────────┴────────────┴──────────┘");
        return sb.toString();
    }
}
