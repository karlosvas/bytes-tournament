package com.equipo2.bytestournament.repository;
 
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.equipo2.bytestournament.model.Message;
 
/**
 * Interfaz que define el repositorio para la entidad Message.
 * Proporciona métodos para acceder a los mensajes de un torneo específico y para una partida específica.
 * 
 * {@link Repository} es una anotación de Spring que indica que esta interfaz es un repositorio
 * y será utilizada para acceder a la base de datos.
 * {@link JpaRepository} es una interfaz de Spring Data JPA que proporciona métodos
 * para realizar operaciones CRUD y consultas en la entidad Match.
 * {@link Optional} es una clase de Java que representa un valor que puede estar presente o no,
 * evitando así el uso de null y proporcionando una forma más segura de manejar valores ausentes.
 */
@Repository
public interface MessageRepository extends JpaRepository  <Message, Long>{

    /**
     * Recupera la lista de mensajes asociados a un torneo específico.
     *
     * @param tournamentId el identificador del torneo
     * @return una lista de mensajes del torneo indicado
     */
    Optional<List<Message>> findByTournamentId(Long tournamentId);

    /**
     * Recupera la lista de mensajes asociados a una partida específica.
     *
     * @param matchId el identificador de la partida
     * @return una lista de mensajes de la partida indicada
     */
    Optional<List<Message>> findByMatchId(Long matchId);

    List<Message> findByTournamentIdOrderByTimestampAsc(Long tournamentId);

    List<Message> findByMatchIdOrderByTimestampAsc(Long matchId);
}

