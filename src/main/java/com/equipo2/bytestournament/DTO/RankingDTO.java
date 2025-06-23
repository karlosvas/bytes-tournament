package com.equipo2.bytestournament.DTO;

import java.util.ArrayList;
import java.util.List;
import com.equipo2.bytestournament.enums.Rank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
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
