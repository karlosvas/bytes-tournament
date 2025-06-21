package com.equipo2.bytestournament.service;

import com.equipo2.bytestournament.DTO.TournamentDTO;
import com.equipo2.bytestournament.enums.ApiResponse;
import com.equipo2.bytestournament.exceptions.RequestException;
import com.equipo2.bytestournament.mapper.TournamentMapper;
import com.equipo2.bytestournament.model.Tournament;
import com.equipo2.bytestournament.model.User;
import com.equipo2.bytestournament.repository.TournamentRepository;
import com.equipo2.bytestournament.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.Optional;

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
}
