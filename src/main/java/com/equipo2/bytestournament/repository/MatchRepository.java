package com.equipo2.bytestournament.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import com.equipo2.bytestournament.model.Match;
 
/**
 * Interfaz que define el repositorio para la entidad Match.
 * Proporciona métodos para acceder a las partidas de un torneo específico
 * y para recuperar partidas de un torneo en una ronda específica.
 * Extiende JpaRepository para heredar métodos CRUD y de consulta.
 * 
 * {@link Repository} es una anotación de Spring que indica que esta interfaz es un repositorio
 * y será utilizada para acceder a la base de datos.
 * {@link JpaRepository} es una interfaz de Spring Data JPA que proporciona métodos
 * para realizar operaciones CRUD y consultas en la entidad Match.
 * {@link Optional} es una clase de Java que representa un valor que puede estar presente o no,
 * evitando así el uso de null y proporcionando una forma más segura de manejar valores ausentes.
 */
@Repository
public interface MatchRepository extends JpaRepository <Match, Long>{
    /**
     * Recupera la lista de partidas asociadas a un torneo específico.
     *
     * @param tournamentId el identificador del torneo
     * @return una lista de partidas que pertenecen al torneo indicado
     */
    Optional<List<Match>> findByTournamentId(Long tournamentId);

    /**
     * Recupera la lista de partidas de un torneo en una ronda específica.
     *
     * @param tournamentId el identificador del torneo
     * @param round el número de ronda
     * @return una lista de partidas del torneo y ronda indicados
     */
    Optional<List<Match>> findByTournamentIdAndRound(Long tournamentId, int round);
}
