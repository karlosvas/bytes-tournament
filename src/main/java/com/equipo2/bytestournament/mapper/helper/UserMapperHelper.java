package com.equipo2.bytestournament.mapper.helper;

import com.equipo2.bytestournament.enums.AuthorityPrivilegies;
import com.equipo2.bytestournament.model.Match;
import com.equipo2.bytestournament.model.Tournament;
import com.equipo2.bytestournament.model.User;
import com.equipo2.bytestournament.repository.TournamentRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class UserMapperHelper {

    private final TournamentRepository tournamentRepository;
    public final Logger logger = LoggerFactory.getLogger(UserMapperHelper.class);
    
    public UserMapperHelper(TournamentRepository tournamentRepository) {
        this.tournamentRepository = tournamentRepository;
    }
    /**
     * Combina las listas de partidas donde el usuario es player1 y player2
     * y las convierte en una única lista de IDs.
     * 
     * @param user El usuario con las listas de partidas
     * @return Lista combinada de IDs de partidas
     */
    public List<Long> combineMatchesToIds(User user) {
        try {
            List<Match> allMatches = new ArrayList<>();
            
            // Añadir partidas donde el usuario es player1
            if (user.getMatchesAsPlayer1() != null && !user.getMatchesAsPlayer1().isEmpty()) {
                allMatches.addAll(user.getMatchesAsPlayer1());
            }
            
            // Añadir partidas donde el usuario es player2
            if (user.getMatchesAsPlayer2() != null && !user.getMatchesAsPlayer2().isEmpty()) {
                allMatches.addAll(user.getMatchesAsPlayer2());
            }
            
            // Convertir la lista combinada a una lista de IDs
            return allMatches.stream()
                    .map(Match::getId)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error al combinar las partidas del usuario: " + user.getUsername(), e);
            return new ArrayList<>(); // Retorna una lista vacía en caso de error
        }
        
    }
    
    /**
     * Convierte una lista de torneos en una lista de IDs de torneos.
     * 
     * @param tournaments Lista de torneos
     * @return Lista de IDs de torneos
     */
    public List<Long> tournamentsToIds(List<Tournament> tournaments) {
        try {
            if (tournaments == null) {
            return null;
        }
        
        return tournaments.stream()
                .map(Tournament::getId)
                .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error al convertir la lista de torneos a IDs", e);
            return new ArrayList<>(); // Retorna una lista vacía en caso de error
        }
    }

    /**
     * Convierte una lista de IDs de torneos en una lista de objetos Tournament.
     * @param tournamentIds
     * @return
     */
    public List<Tournament> idsToTournaments(List<Long> tournamentIds) {
        try {
            if (tournamentIds == null) {
            return null;
        }
        
        return tournamentIds.stream()
                .map(id -> tournamentRepository.findById(id).orElse(null))
                .filter(tournament -> tournament != null)
                .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error al convertir la lista de IDs de torneos a objetos Tournament", e);
            return new ArrayList<>(); // Retorna una lista vacía en caso de error
        }
    }

    /**
     * Convierte una lista de privilegios a un conjunto de nombres de privilegios.
     * Utiliza AuthorityPrivilegies para mapear los IDs a nombres.
     * 
     * @param privileges Lista de IDs de privilegios
     * @return Set de nombres de privilegios
     */
    public Set<String> privilegesToNames(Set<AuthorityPrivilegies> privileges) {
        try {
            return privileges.stream()
            .map(Enum::name)  // Usa el método estándar name() de los enums
            .collect(Collectors.toSet());
        } catch (Exception e) {
            logger.error("Error al convertir la lista de privilegios a nombres", e);
            return Set.of(); // Retorna un conjunto vacío en caso de error
        }
        
    }

    /**
     * Convierte un conjunto de nombres de privilegios a un conjunto de AuthorityPrivilegies.
     * Utiliza AuthorityPrivilegies para mapear los nombres a objetos AuthorityPrivilegies.
     * 
     * @param privilegeNames
     * @return
     */
    public Set<AuthorityPrivilegies>  namesToPrivileges(Set<String> privilegeNames) {
        try {
            return privilegeNames.stream()
            .map(name -> AuthorityPrivilegies.valueOf(name))
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());
        } catch (Exception e) {
            logger.error("Error al convertir el conjunto de nombres de privilegios a AuthorityPrivilegies", e);
            return Set.of(); // Retorna un conjunto vacío en caso de error
        }
    }
}