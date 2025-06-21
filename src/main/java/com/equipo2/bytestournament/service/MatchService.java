package com.equipo2.bytestournament.service;

import com.equipo2.bytestournament.DTO.MatchDTO;
import com.equipo2.bytestournament.enums.ApiResponse;
import com.equipo2.bytestournament.enums.Result;
import com.equipo2.bytestournament.exceptions.RequestException;
import com.equipo2.bytestournament.mapper.MatchMapper;
import com.equipo2.bytestournament.model.Match;
import com.equipo2.bytestournament.model.Tournament;
import com.equipo2.bytestournament.model.User;
import com.equipo2.bytestournament.repository.MatchRepository;
import com.equipo2.bytestournament.repository.TournamentRepository;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import static com.equipo2.bytestournament.model.Match.UMBRAL;

@Service
public class MatchService {
    private final MatchRepository matchRepository;
    private final MatchMapper matchMapper;
    private final TournamentRepository tournamentRepository;
    public final Logger logger = Logger.getLogger(MatchService.class.getName());

    public MatchService(MatchRepository matchRepository, MatchMapper matchMapper, TournamentRepository tournamentRepository) {
        this.matchRepository = matchRepository;
        this.matchMapper = matchMapper;
        this.tournamentRepository = tournamentRepository;
    }

    /**
     * Genera los matches para un torneo especifico.
     * Se emparejan los jugadores de forma aleatoria, teniendo en cuenta que la diferencia de puntos entre ellos no supere el umbral.
     * Si no hay suficientes jugadores, se lanza una excepción.
     * Si el torneo no existe, se lanza una excepción.
     * Si se generan los matches correctamente, se devuelve una lista de MatchDTO con los matches generados.
     * 
     * @param tournamentID
     * @return
     */
    public List<MatchDTO> generateMatches(Long tournamentID) {
        try{
            // Obtenemos el torneo que le estamos pasando
            Optional<Tournament> tournamentOptional = tournamentRepository.findById(tournamentID);

            if (tournamentOptional.isEmpty())
                throw new RequestException(ApiResponse.NOT_FOUND, "No se ha encontrado el torneo", "El torneo con id " + tournamentID + " no existe");

            Tournament tournament = tournamentOptional.get();
            logger.info("Generando matches para el torneo: " + tournament.getName());

            // Obtenemos la lista de jugadores del torneo
            List<User> players = tournament.getPlayers();
            if (players.size() < 2)
                throw new RequestException(ApiResponse.UNPROCESSABLE_ENTITY);

            // Actualizamos la ronda del torneo
            tournament.setRounds(tournament.getRounds() + 1);

            // Copiamos los jugadores para la logica de emparejamiento y no modificar la lista original
            List<User> emparejatedPlayers = new ArrayList<>(players);
            // Genemamos todos los matches de la ronda actual
            while (emparejatedPlayers.size() >= 2) {
                List<User> emparejatedMatch = this.matchUsers(emparejatedPlayers);

                // Obtenemos los dos jugadores emparejados
                User player1 = emparejatedMatch.get(0);
                User player2 = emparejatedMatch.get(1);

                // Eliminamos los jugadores de la lista de emparejamiento
                Match newMatch = Match.builder()
                        .tournament(tournament)
                        .player1(player1)
                        .player2(player2)
                        .result(Result.PENDING)
                        .round(tournament.getRounds())
                        .build();

                // Añadimos un nuevo match a la lista de matches del torneo
                List<Match> tournamentList = tournament.getMatches();
                tournamentList.add(newMatch);

                // Añadimos el match a la base de datos
                matchRepository.save(newMatch);
                logger.info("Match generado: " + player1.getEmail() + " vs " + player2.getEmail());
            }

            // Guardamos los cambios del torneo
            tournamentRepository.save(tournament);
            logger.info("Matches generados para el torneo: " + tournament.getName());

            return matchMapper.matchListToMatchDTOList(tournament.getMatches());
        } catch (RequestException error){
            throw error;
        } catch (Exception error) {
            throw new RequestException(ApiResponse.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Empareja dos jugadores de forma aleatoria, teniendo en cuenta que la diferencia de puntos entre ellos no supere el umbral.
     * Si no hay suficientes jugadores, se lanza una excepción.
     * Si no hay jugadores que cumplan el criterio de puntos, se lanza una excepción.
     * 
     * @param allPlayers
     * @return
     */
    public List<User> matchUsers(List<User> allPlayers) {
        // Comprobamos que haya al menos 2 jugadores para emparejar
        if(allPlayers.size() < 2)
            throw new RequestException(ApiResponse.UNPROCESSABLE_ENTITY);

        // Obtenemos el jugador 1 de forma aleatoria
        User player1 = allPlayers.get((int) (Math.random() * allPlayers.size()));
        allPlayers.remove(player1);

        // Filtramos por todos los jugadores que tengan menos o mas de 100 puntos de diferencia
        List<User> players = allPlayers.stream()
                .filter(player -> Math.abs(player.getPoints() - player1.getPoints()) <= UMBRAL)
                .toList();

        // Obtenemos el jugador 2 de forma aleatoria
        User player2 = players.get((int) (Math.random() * players.size()));
        allPlayers.remove(player2);

        return List.of(player1, player2);
    }

    /**
     * Comprueba si un match existe en la base de datos.
     * Si el match no existe, lanza una excepción.
     * Si el match existe, devuelve un mensaje informativo con los jugadores del match.
     * 
     * @param id
     */
    public MatchDTO checkMatch(Long id) {
        // Comprobamos si el match existe
        Optional<Match> matchOptional = matchRepository.findById(id);
        if (matchOptional.isEmpty())
            throw new RequestException(ApiResponse.NOT_FOUND, "No se ha encontrado el match", "El match con id " + id + " no existe");

        Match match = matchOptional.get();
        logger.info("Match encontrado: " + match.getPlayer1().getEmail() + " vs " + match.getPlayer2().getEmail());
        
        return matchMapper.matchToMatchDTO(match);
    }

    /**
     * Actualiza el resultado de un match.
     * Si el match no existe, lanza una excepción.
     * Si el resultado es PENDING, lanza una excepción.
     * Si el resultado es WIN o LOSE, actualiza el resultado del match y devuelve un mensaje informativo.
     * 
     * @param id
     * @return
     */
    public MatchDTO updateMatchResult(Long id, MatchDTO macthDTO) {
        // Comprobamos si el match existe
        Optional<Match> matchOptional = matchRepository.findById(id);
        if (matchOptional.isEmpty())
            throw new RequestException(ApiResponse.NOT_FOUND, "No se ha encontrado el match", "El match con id " + id + " no existe");

        Match match = matchOptional.get();

        // Comprobamos que el id proiporcionado sea el mismo que elq ue queremos actualizar
        if (!match.getId().equals(id))
            throw new RequestException(ApiResponse.UNPROCESSABLE_ENTITY, "El id del match no coincide", "El id del match proporcionado no coincide con el id del match a actualizar");

        // Actualizamos el resultado del match
        Match matchUpdated = matchMapper.matchDtoToMatch(macthDTO);
        matchUpdated.setId(id);

        matchRepository.save(matchUpdated);
        logger.info("Resultado del match actualizado: " + matchUpdated.getPlayer1().getEmail() + " vs " + matchUpdated.getPlayer2().getEmail());

        return matchMapper.matchToMatchDTO(matchUpdated);
    }
}
