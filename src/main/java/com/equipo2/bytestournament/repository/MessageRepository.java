package com.equipo2.bytestournament.repository;
 

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
 
@Repository
public interface MessageRepository extends JpaRepository  <Message, Long>{

    /**
     * Recupera la lista de mensajes asociados a un torneo específico.
     *
     * @param tournamentId el identificador del torneo
     * @return una lista de mensajes del torneo indicado
     */
    List<Message> findByTournamentId(Long tournamentId);

    /**
     * Recupera la lista de mensajes asociados a una partida específica.
     *
     * @param matchId el identificador de la partida
     * @return una lista de mensajes de la partida indicada
     */
    List<Message> findByMatchId(Long matchId);
}

