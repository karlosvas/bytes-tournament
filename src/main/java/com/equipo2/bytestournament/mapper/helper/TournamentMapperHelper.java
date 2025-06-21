package com.equipo2.bytestournament.mapper.helper;

import com.equipo2.bytestournament.model.Match;
import com.equipo2.bytestournament.model.User;
import com.equipo2.bytestournament.repository.MatchRepository;
import com.equipo2.bytestournament.repository.UserRepository;
import org.mapstruct.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Helper para asistir al TournamentMapper con conversiones complejas
 * entre entidades y DTOs.
 */
@Component
public class TournamentMapperHelper {

    private final UserRepository userRepository;
    private final MatchRepository matchRepository;
    private final Logger logger = LoggerFactory.getLogger(TournamentMapperHelper.class);

    public TournamentMapperHelper(UserRepository userRepository, MatchRepository matchRepository) {
        this.userRepository = userRepository;
        this.matchRepository = matchRepository;
    }

    /**
     * Convierte una lista de partidas a una lista de IDs de partidas.
     * 
     * @param matches Lista de partidas
     * @return Lista de IDs de partidas
     */
    public List<Long> matchesToIds(List<Match> matches) {
        try {
            if (matches == null) {
                return null;
            }
            
            return matches.stream()
                    .map(Match::getId)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error al convertir la lista de partidas a IDs", e);
            return new ArrayList<>();
        }
    }

    /**
     * Convierte una lista de IDs de partidas a una lista de partidas.
     * 
     * @param matchIds Lista de IDs de partidas
     * @return Lista de partidas
     */
    public List<Match> idsToMatches(List<Long> matchIds) {
        try {
            if (matchIds == null) {
                return null;
            }
            
            return matchIds.stream()
                    .map(id -> matchRepository.findById(id).orElse(null))
                    .filter(match -> match != null)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error al convertir la lista de IDs de partidas a objetos Match", e);
            return new ArrayList<>();
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
            if (players == null) {
                return null;
            }
            
            return players.stream()
                    .map(User::getId)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error al convertir la lista de jugadores a IDs", e);
            return new ArrayList<>();
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
            if (playerIds == null) {
                return null;
            }
            
            return playerIds.stream()
                    .map(id -> userRepository.findById(id).orElse(null))
                    .filter(user -> user != null)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error al convertir la lista de IDs de usuarios a objetos User", e);
            return new ArrayList<>();
        }
    }
}