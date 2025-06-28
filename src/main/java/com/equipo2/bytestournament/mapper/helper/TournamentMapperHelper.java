package com.equipo2.bytestournament.mapper.helper;

import com.equipo2.bytestournament.enums.ApiResponse;
import com.equipo2.bytestournament.exceptions.RequestException;
import com.equipo2.bytestournament.model.Match;
import com.equipo2.bytestournament.model.User;
import com.equipo2.bytestournament.repository.MatchRepository;
import com.equipo2.bytestournament.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Helper para asistir al TournamentMapper con conversiones complejas
 * entre entidades y DTOs.
 * Este helper proporciona métodos para convertir entre IDs y entidades
 * de tipo Tournament y User, manejando excepciones y errores de manera adecuada.
 * 
 * @Component Anotación de Spring que indica que esta clase es un componente
 * que puede ser inyectado en otros componentes de la aplicación.
 */
@Component
public class TournamentMapperHelper {

    /**
     * userRepository Repositorio para acceder a los usuarios.
     * matchRepository Repositorio para acceder a las partidas.
     * Logger  para registrar mensajes de error y depuración.
     */
    private final UserRepository userRepository;
    private final MatchRepository matchRepository;
    private final Logger logger = LoggerFactory.getLogger(TournamentMapperHelper.class);

    public TournamentMapperHelper(UserRepository userRepository, MatchRepository matchRepository) {
        this.userRepository = userRepository;
        this.matchRepository = matchRepository;
    }

    /**
     * Convierte una lista de Match a una lista de IDs de partidas.
     * 
     * @param matchesList Lista de partidas
     * @return Lista de IDs de partidas
     */
    public List<Long> matchesToIds(List<Match> matchesList) {
        try {
            if (matchesList == null)
                throw new Exception();
            
            return matchesList.stream()
                    .map(Match::getId)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error al convertir la lista de partidas a IDs", e);
            throw new RequestException(ApiResponse.NOT_FOUND, "Match no encontrado", "No se pudo convertir la lista de partidas a IDs");
        }
    }

    /**
     * Convierte una lista de IDs de partidas a una lista de Match.
     * 
     * @param matchIds Lista de IDs de partidas
     * @return Lista de partidas (Match)
     */
    public List<Match> idsToMatches(List<Long> matchIds) {
        try {
            if (matchIds == null)
                throw new Exception();
            
            return matchIds.stream()
                    .map(id -> matchRepository.findById(id).orElse(null))
                    .filter(match -> match != null)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error al convertir la lista de IDs de partidas a objetos Match", e);
            throw new RequestException(ApiResponse.NOT_FOUND, "Match no encontrado", "No se pudo convertir la lista de IDs de partidas a objetos Match");
        }
    }

    /**
     * Convierte una lista de jugadores a una lista de IDs de usuarios.
     * 
     * @param players Lista de jugadores (usuarios)
     * @return Lista de IDs de usuarios
     */
    public List<Long> playersToIds(List<User> players) {
        try {
            if (players == null)
                throw new Exception();
            
            return players.stream()
                    .map(User::getId)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error al convertir la lista de jugadores a IDs", e);
            throw new RequestException(ApiResponse.NOT_FOUND, "Usuario no encontrado", "No se pudo convertir la lista de jugadores a IDs");
        }
    }

    /**
     * Convierte una lista de IDs de usuarios a una lista de jugadores (usuarios).
     * 
     * @param playerIds Lista de IDs de usuarios
     * @return Lista de jugadores (usuarios)
     */
    public List<User> idsToPlayers(List<Long> playerIds) {
        try {
            if (playerIds == null) 
                throw new Exception();
            
            return playerIds.stream()
                    .map(id -> userRepository.findById(id).orElse(null))
                    .filter(user -> user != null)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error al convertir la lista de IDs de usuarios a objetos User", e);
            throw new RequestException(ApiResponse.NOT_FOUND, "Usuario no encontrado", "No se pudo convertir la lista de IDs de usuarios a objetos User");
        }
    }
}