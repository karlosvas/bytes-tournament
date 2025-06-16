package com.equipo2.bytestournament.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
 
 

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

    /**
     * Recupera la lista de partidas donde un usuario es jugador 1 o jugador 2.
     *
     * @param player1Id el identificador del primer jugador
     * @param player2Id el identificador del segundo jugador
     * @return una lista de partidas donde el usuario participa como jugador 1 o 2
     */
    List<Match> findByPlayer1IdOrPlayer2Id(Long player1Id, Long player2Id);



}
