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
import java.util.concurrent.ThreadLocalRandom;
/**
 * MatchService es un servicio que se encarga de gestionar los matches de un torneo.
 * Proporciona métodos para generar matches, emparejar jugadores, comprobar la existencia de un
 * match y actualizar el resultado de un match.
 * 
 *{@link Service} es una anotación de Spring que indica que esta clase es un servicio de spring
 * y será utilizada para realizar operaciones de negocio relacionadas con los matches.
 */
@Service
public class MatchService {
    /**
     * matchRepository Repositorio para acceder a los matches.
     * matchMapper Mapeador para convertir entre Match y MatchDTO.
     * tournamentRepository Repositorio para acceder a los torneos.
     * Logger para registrar mensajes de error y depuración.
     */
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
     * @param tournamentID ID del torneo para el cual se quieren generar los matches.
     * @return Lista de MatchDTO con los matches generados.
     */
    public List<MatchDTO> generateMatches(Long tournamentID) {
        try{
            // Obtenemos el torneo que le estamos pasando
            Optional<Tournament> tournamentOptional = tournamentRepository.findById(tournamentID);

            Tournament tournament = tournamentOptional.get();
            logger.info("Generando matches para el torneo: " + tournament.getName());

            // Obtenemos la lista de jugadores del torneo
            List<User> players = tournament.getPlayers();
            if (players.size() < 2)
                throw new RequestException(ApiResponse.UNPROCESSABLE_ENTITY, "Entidad No Procesable", "Necesitas al menos 2 jugadores en el torneo para generar matches");

            // Comprobamos que el número de jugadores sea par
            if(players.size() % 2 != 0)
                throw new RequestException(ApiResponse.UNPROCESSABLE_ENTITY, "Entidad No Procesable", "El número de jugadores debe ser par para generar matches, elimina un usuario o añade uno más");
            
            // Actualizamos la ronda del torneo
            tournament.setRounds(tournament.getRounds() + 1);
            logger.info("Ronda actual del torneo: " + tournament.getRounds());

            // Copiamos los jugadores para la logica de emparejamiento y no modificar la lista original
            List<User> emparejatedPlayers = new ArrayList<>(players);
            logger.info("Jugadores a emparejar: " + emparejatedPlayers.size());

            // Genemamos todos los matches de la ronda actual
            while (emparejatedPlayers.size() >= 2) {
                List<User> emparejatedMatch = this.matchUsers(emparejatedPlayers);

                // Comprobamos que se hayan emparejado correctamente dos jugadores
                if(emparejatedMatch.size() != 2)
                    throw new RequestException(ApiResponse.UNPROCESSABLE_ENTITY, "Entidad No Procesable", "No se han podido emparejar los jugadores correctamente, por favor añade más jugadores al torneo.");

                User player1 = emparejatedMatch.get(0);
                User player2 =emparejatedMatch.get(1);

                // Eliminamos los jugadores de la lista de emparejamiento
                Match newMatch = Match.builder()
                        .tournament(tournament)
                        .player1(player1)
                        .player2(player2)
                        .result(Result.PENDING)
                        .round(tournament.getRounds())
                        .build();

                logger.info("Emparejados: " + player1.getEmail() + " vs " + player2.getEmail());
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
     * Empareja dos jugadores de forma parcialmente aleatoria en un torneo, teniendo en cuenta que la diferencia de puntos entre ellos no supere el umbral.
     * Si no hay suficientes jugadores, se lanza una excepción.
     * Si no hay jugadores que cumplan el criterio de puntos, se lanza una excepción.
     * 
     * @param allPlayers Lista de todos los jugadores del torneo.
     * @return Lista de dos jugadores emparejados.
     */
    public List<User> matchUsers(List<User> allPlayers) throws RequestException {
        try {
            // Comprobamos que haya al menos 2 jugadores para emparejar
            if(allPlayers.size() < 2)
                throw new Exception();
            // Obtenemos el jugador 1 de forma aleatoria
            int randomPlayer1 = ThreadLocalRandom.current().nextInt(allPlayers.size());
            User player1 = allPlayers.get(randomPlayer1);
            allPlayers.remove(player1);
            
            // Filtramos por todos los jugadores que tengan menos o mas de 100 puntos de diferencia
            List<User> possiblePlayers = allPlayers.stream()
            .filter(player -> Math.abs(player.getPoints() - player1.getPoints()) <= UMBRAL)
            .toList();
            
            User player2 = null;
            if (possiblePlayers.isEmpty()){
                // Si no hay jugadores que cumplan el criterio de puntos, obtenemos un jugador aleatorio de los disponibles que mas se acerce en cuanto a puntos, que ademas sea diferente al jugador 1
                player2 = allPlayers.stream()
                .filter(player -> !player.equals(player1))
                .min((a, b) -> Integer.compare(
                    Math.abs(a.getPoints() - player1.getPoints()),
                    Math.abs(b.getPoints() - player1.getPoints())
                ))
                .orElseThrow(() -> new Exception());  
            } else{
                // Obtenemos el jugador 2 de forma aleatoria, sin contar con el jugador 1
                Integer randomPlayer2 = ThreadLocalRandom.current().nextInt(possiblePlayers.size());
                player2 = possiblePlayers.get(randomPlayer2);
            }

            if(player2 == null) throw new Exception();
            
            allPlayers.remove(player2);
            return List.of(player1, player2);
        } catch (Exception e) {
            throw new RequestException(ApiResponse.UNPROCESSABLE_ENTITY, "Entidad No Procesable",
            "La solicitud contiene datos que no se pueden procesar debido a que se necesitan al menos 2 jugadores para emparejar");
        }
    }

    /**
     * Comprueba si un match existe en la base de datos.
     * Si el match no existe, lanza una excepción.
     * Si el match existe, devuelve un mensaje informativo con los jugadores del match.
     * 
     * @param id ID del match a comprobar.
     */
    public MatchDTO checkMatch(Long matchId) {
        // Comprobamos si el match existe
        Optional<Match> matchOptional = matchRepository.findById(matchId);
        if (matchOptional.isEmpty())
            throw new RequestException(ApiResponse.NOT_FOUND, "No se ha encontrado el match", "El match con id " + matchId + " no existe");

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
     * @param matchID ID del match a actualizar.
     * @param macthDTO DTO con el resultado del match a actualizar.
     * @return MatchDTO con el match actualizado.
     */
    public MatchDTO updateMatchResult(Long matchID, MatchDTO macthDTO) throws RequestException {
        // Comprobamos si el match existe
        Optional<Match> matchOptional = matchRepository.findById(matchID);
        if (matchOptional.isEmpty())
            throw new RequestException(ApiResponse.NOT_FOUND, "No se ha encontrado el match", "El match con id " + matchID + " no existe");

        // Actualizamos el resultado del match
        Match matchUpdated = matchMapper.matchDtoToMatch(macthDTO);
        matchUpdated.setId(matchID);

        if(matchUpdated.getPlayer1() == null || matchUpdated.getPlayer2() == null)
            throw new RequestException(ApiResponse.UNPROCESSABLE_ENTITY, "Entidad No Procesable", "El match debe tener dos jugadores validos");
        if(matchUpdated.getTournament() == null)
            throw new RequestException(ApiResponse.UNPROCESSABLE_ENTITY, "Entidad No Procesable", "El match debe pertenecer a un torneo valido");

        matchRepository.save(matchUpdated);

        logger.info("Resultado del match actualizado: " + matchUpdated.getResult());
        return matchMapper.matchToMatchDTO(matchUpdated);
    }

    public List<MatchDTO> getAllMatches() {
        // Obtenemos todos los matches de la base de datos
        List<Match> matches = matchRepository.findAll();

        logger.info("Obtenidos " + matches.size() + " matches de la base de datos");

        // Convertimos los matches a MatchDTO
        return matchMapper.matchListToMatchDTOList(matches);
    }

    /**
     * Elimina un match de la base de datos.
     * Si el match no existe, lanza una excepción.
     * 
     * @param matchID ID del match a eliminar.
     */
    public void deleteMatch(Long matchID) {
        // Comprobamos si el match existe
        Optional<Match> matchOptional = matchRepository.findById(matchID);
        if (matchOptional.isEmpty())
            throw new RequestException(ApiResponse.NOT_FOUND, "No se ha encontrado el match", "El match con id " + matchID + " no existe");

        Match match = matchOptional.get();
        logger.info("Match eliminado: " + match.getPlayer1().getEmail() + " vs " + match.getPlayer2().getEmail());
        matchRepository.delete(match);
    }
}
