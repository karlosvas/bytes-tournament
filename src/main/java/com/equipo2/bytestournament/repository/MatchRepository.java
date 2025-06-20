package com.equipo2.bytestournament.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.equipo2.bytestournament.model.Match;
 

@Repository
public interface MatchRepository extends JpaRepository <Match, Long>{
    /**
     * Recupera la lista de partidas asociadas a un torneo específico.
     *
     * @param tournamentId el identificador del torneo
     * @return una lista de partidas que pertenecen al torneo indicado
     */
    List<Match> findByTournamentId(Long tournamentId);

    /**
     * Recupera la lista de partidas de un torneo en una ronda específica.
     *
     * @param tournamentId el identificador del torneo
     * @param round el número de ronda
     * @return una lista de partidas del torneo y ronda indicados
     */
    List<Match> findByTournamentIdAndRound(Long tournamentId, int round);
}
