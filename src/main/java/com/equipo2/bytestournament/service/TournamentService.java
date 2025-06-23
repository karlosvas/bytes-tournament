package com.equipo2.bytestournament.service;

import com.equipo2.bytestournament.DTO.RankingDTO;
import com.equipo2.bytestournament.DTO.RankingDetailsDTO;
import com.equipo2.bytestournament.DTO.TournamentDTO;
import com.equipo2.bytestournament.enums.ApiResponse;
import com.equipo2.bytestournament.exceptions.RequestException;
import com.equipo2.bytestournament.mapper.TournamentMapper;
import com.equipo2.bytestournament.model.Match;
import com.equipo2.bytestournament.model.Tournament;
import com.equipo2.bytestournament.model.User;
import com.equipo2.bytestournament.repository.TournamentRepository;
import com.equipo2.bytestournament.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import com.equipo2.bytestournament.enums.Result;


@Service
public class TournamentService {

    private final TournamentRepository tournamentRepository;
    private final TournamentMapper tournamentMapper;
    private final UserRepository userRepository;
    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    public TournamentService(TournamentRepository tournamentRepository, TournamentMapper tournamentMapper, UserRepository userRepository) {
        this.tournamentRepository = tournamentRepository;
        this.tournamentMapper = tournamentMapper;
        this.userRepository = userRepository;
    }

    public TournamentDTO findTournamentById(Long id){
        Optional<Tournament> tournamentOptional = tournamentRepository.findById(id);

        if(!tournamentOptional.isPresent())
            throw new RequestException(ApiResponse.NOT_FOUND, "Tournament no encontrado", "No se encontro un tournament con esa ID");

        Tournament tournament = tournamentOptional.get();
        return tournamentMapper.tournamentToTournamentDTO(tournament);
    }

    public TournamentDTO createTournament(TournamentDTO entity) {
        // Aquí se debería implementar la lógica para crear un torneo a partir de la entidad proporcionada
        // Por ahora, simplemente retornamos un TournamentDTO vacío como ejemplo
        Tournament tournament = tournamentMapper.tournamentDtoToTournament(entity);
        // Aquí se deberían establecer los valores del torneo según la entidad recibida
        tournamentRepository.save(tournament);
        return tournamentMapper.tournamentToTournamentDTO(tournament);
    }

    public TournamentDTO addPlayerToTournament(Long tournamentId, String userName) {
        // Validamos si existe el torneo y el usuario
        Optional<Tournament> tournamentOptional = tournamentRepository.findById(tournamentId);

        if(tournamentOptional.isEmpty()) 
            throw new RequestException(ApiResponse.NOT_FOUND, "Tournament no encontrado", "No se encontro un torneo con esa ID");

        Optional<User> userOptional = userRepository.findByUsername(userName);

        if(userOptional.isEmpty()) 
            throw new RequestException(ApiResponse.NOT_FOUND, "Usuario no encontrado", "No se encontro un usuario con esa ID");
        
        // Añadmios las referencias del usuario al torneo viceversa
        User user = userOptional.get();
        Tournament tournament = tournamentOptional.get();

        // Obtenemos los usuarrios del torneo para verrificar que ya estaba apuntado
        List<User> listUser = tournament.getPlayers();
        if(listUser.contains(user))
            throw new RequestException(ApiResponse.DUPLICATE_RESOURCE, "Recurso duplicado", "El usuario ya esta apuntado al torneo actual");

        // En el torneo hay un nuevo usuario
        tournament.getPlayers().add(userOptional.get());
        // Un nuevo usuaurio pertenece a un torneo
        user.getTournaments().add(tournamentOptional.get());

        // Guardamos en la base de datos todos los cambios
        Tournament torurnamentSaved = tournamentRepository.save(tournament);
        userRepository.save(user);

        logger.info("Usuario {} se ha unido al torneo {}", user.getEmail(), tournament.getName());

        return tournamentMapper.tournamentToTournamentDTO(torurnamentSaved);
    }

    public RankingDTO getClassification(Long tournamentId) {
        Optional<Tournament> tournamentOptional = tournamentRepository.findById(tournamentId);

        if(tournamentOptional.isEmpty())
            throw new RequestException(ApiResponse.NOT_FOUND, "Tournament no encontrado", "No se encontro un torneo con esa ID");
        
        Tournament tournament = tournamentOptional.get();

        List<User> sortedPlayers = tournament.getPlayers()
        .stream()
        .sorted(Comparator.comparingInt(User::getPoints).reversed())
        .toList();

        RankingDTO classification = new RankingDTO();
        for (User player : sortedPlayers) {
            RankingDTO.InnerClassificationDTO classificationPlayer = new RankingDTO.InnerClassificationDTO(player.getUsername(), player.getRank(), player.getPoints());
            classification.getPlayers().add(classificationPlayer);
        }

        return classification;
    }

    public List<RankingDetailsDTO> getRankingDetails(Long tournamentId) {
        Optional<Tournament> tournamentOptional = tournamentRepository.findById(tournamentId);

        
        if(tournamentOptional.isEmpty())
            throw new RequestException(ApiResponse.NOT_FOUND, "Tournament no encontrado", "No se encontro un torneo con esa ID");

        List<User> userList = tournamentOptional.get().getPlayers();

        List<RankingDetailsDTO> listDetailsRanking = new ArrayList<>();
        for (User user : userList) {
            logger.info("Detalle sde usuario: {}", user.getUsername());

            Optional<User> userOptional = userRepository.findByUsername(user.getUsername());

            if(userOptional.isEmpty())
                throw new RequestException(ApiResponse.NOT_FOUND, "Usuario no encontrado", "No se encontro un usuario con ese nombre");

            User actualUser = userOptional.get();
            List<Match> listMatches = actualUser.getMatches();

            Integer totalWins = 0, totalLosses = 0, totalDraws = 0, totalPoints = 0;

            for (Match match : listMatches) {
                Result result = match.getResult();
                if(result == Result.WIN){
                    totalWins++;
                    totalPoints+=10;
                } else if(result == Result.LOSE) {
                    totalLosses++;
                    if(totalPoints > 10)
                        totalPoints-=10;
                } else if(result == Result.DRAW){
                    totalDraws++;
                }
            }

            RankingDetailsDTO detailsRankingDTO = new RankingDetailsDTO(user.getUsername(), totalWins, totalLosses, totalDraws, totalPoints);
            listDetailsRanking.add(detailsRankingDTO);
        }
       
        return listDetailsRanking;
    }
}
