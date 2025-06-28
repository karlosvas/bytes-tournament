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
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import com.equipo2.bytestournament.enums.Result;
import com.equipo2.bytestournament.enums.Role;


/**
 * TournamentService es un servicio que se encarga de gestionar los torneos.
 * Proporciona métodos para buscar, crear torneos, añadir jugadores a un torneo,
 * obtener la clasificación de un torneo y obtener detalles del ranking de los jugadores.
 * 
 * {@link Service} es una anotación de Spring que indica que esta clase es un servicio de spring
 * y será utilizada para realizar operaciones de negocio relacionadas con los torneos.
 */
@Service
public class TournamentService {

    /**
     * tournamentRepository Repositorio para acceder a los torneos.
     * tournamentMapper Mapeador para convertir entre Tournament y TournamentDTO.
     * userRepository Repositorio para acceder a los usuarios.
     * Logger para registrar mensajes de error y depuración.
     */
    private final TournamentRepository tournamentRepository;
    private final TournamentMapper tournamentMapper;
    private final UserRepository userRepository;
    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    public TournamentService(TournamentRepository tournamentRepository, TournamentMapper tournamentMapper, UserRepository userRepository) {
        this.tournamentRepository = tournamentRepository;
        this.tournamentMapper = tournamentMapper;
        this.userRepository = userRepository;
    }

    /**
     * Busca un torneo por su ID y devuelve un TournamentDTO.
     * Este método debería implementar la lógica para buscar un torneo en la base de datos
     * 
     * @param tournamentId ID del torneo a buscar.
     * @return TournamentDTO con la información del torneo encontrado.
     */
    public TournamentDTO findTournamentById(Long tournamentId){
        Optional<Tournament> tournamentOptional = tournamentRepository.findById(tournamentId);

        if(!tournamentOptional.isPresent())
            throw new RequestException(ApiResponse.NOT_FOUND, "Tournament no encontrado", "No se encontro un tournament con esa ID");

        Tournament tournament = tournamentOptional.get();
        return tournamentMapper.tournamentToTournamentDTO(tournament);
    }

    /**
     * Crea un nuevo torneo a partir de un TournamentDTO.
     * Este método debería implementar la lógica para crear un torneo a partir de la entidad proporcionada.
     * 
     * @param tournamentDTO TournamentDTO que contiene la información del torneo a crear.
     * @return
     */
    public TournamentDTO createTournament(TournamentDTO tournamentDTO) {
        // Aquí se debería implementar la lógica para crear un torneo a partir de la entidad proporcionada
        // Por ahora, simplemente retornamos un TournamentDTO vacío como ejemplo
        Tournament tournament = tournamentMapper.tournamentDtoToTournament(tournamentDTO);

        // Validamos que el torneo cumple con las reglas
       this.validateTournament(tournament);

        // Aquí se deberían establecer los valores del torneo según la entidad recibida
        tournamentRepository.save(tournament);
        return tournamentMapper.tournamentToTournamentDTO(tournament);
    }

    /**
     * Añade un jugador a un torneo existente.
     * Este método busca el torneo por su ID y el usuario por su nombre de usuario.
     * Si ambos existen, añade el usuario al torneo y viceversa.
     * Si el torneo o el usuario no existen, lanza una excepción.
     * Si el usuario ya está apuntado al torneo, lanza una excepción.
     * 
     * @param tournamentId ID del torneo al que se quiere añadir el jugador.
     * @param userName Nombre de usuario del jugador que se quiere añadir al torneo.
     * @return TournamentDTO con la información del torneo actualizado.
     */
    public TournamentDTO addPlayerToTournament(Long tournamentId, String userName) {
        // Validamos si existe el torneo y el usuario
        Optional<Tournament> tournamentOptional = tournamentRepository.findById(tournamentId);

        if(tournamentOptional.isEmpty()) 
            throw new RequestException(ApiResponse.NOT_FOUND, "Tournament no encontrado", "No se encontro un torneo con esa ID");

        Optional<User> userOptional = userRepository.findByUsername(userName);

        if(userOptional.isEmpty()) 
            throw new RequestException(ApiResponse.NOT_FOUND, "Usuario no encontrado", "No se encontro un usuario con esa ID");
        
        // Añadimos las referencias del usuario al torneo viceversa
        User user = userOptional.get();
        Tournament tournament = tournamentOptional.get();

        // Obtenemos los usuarrios del torneo para verificar que ya estaba apuntado
        List<User> listUser = tournament.getPlayers();
        if(listUser.contains(user))
            throw new RequestException(ApiResponse.DUPLICATE_RESOURCE, "Recurso duplicado", "El usuario ya esta apuntado al torneo actual");

        // En el torneo hay un nuevo usuario
        tournament.getPlayers().add(userOptional.get());
        // Un nuevo usuario pertenece a un torneo
        user.getTournaments().add(tournamentOptional.get());

        // Validamos que el torneo cumple con las reglas
        this.validateTournament(tournament);
        
        // Guardamos en la base de datos todos los cambios
        Tournament torurnamentSaved = tournamentRepository.save(tournament);
        userRepository.save(user);

        logger.info("Usuario {} se ha unido al torneo {}", user.getEmail(), tournament.getName());

        return tournamentMapper.tournamentToTournamentDTO(torurnamentSaved);
    }

    /**
     * Obtiene la clasificación de un torneo dado su ID.
     * Este método busca el torneo por su ID y ordena a los jugadores por sus puntos en orden descendente.
     * Luego, crea un RankingDTO con la información de los jugadores ordenados.
     * Si el torneo no existe, lanza una excepción.
     * 
     * @param tournamentId ID del torneo para el cual se quiere obtener la clasificación.
     * @return RankingDTO con la clasificación de los jugadores del torneo.
     */
    public RankingDTO getClassification(Long tournamentId) {
        // Obtenemos el torneo por su ID de la DB
        Optional<Tournament> tournamentOptional = tournamentRepository.findById(tournamentId);

        if(tournamentOptional.isEmpty())
            throw new RequestException(ApiResponse.NOT_FOUND, "Tournament no encontrado", "No se encontro un torneo con esa ID");
        
        Tournament tournament = tournamentOptional.get();

        // Ordenamos los jugadores del torneo por sus puntos en orden descendente
        List<User> sortedPlayers = tournament.getPlayers()
        .stream()
        .sorted(Comparator.comparingInt(User::getPoints).reversed())
        .toList();

        // Creamos el RankingDTO con la información de los jugadores ordenados
        RankingDTO classification = new RankingDTO();
        for (User player : sortedPlayers) {
            RankingDTO.InnerClassificationDTO classificationPlayer = new RankingDTO.InnerClassificationDTO(player.getUsername(), player.getRank(), player.getPoints());
            classification.getPlayers().add(classificationPlayer);
        }

        return classification;
    }

    /**
     * Obtiene los detalles del ranking de un torneo dado su ID.
     * Este método busca el torneo por su ID y obtiene la lista de jugadores.
     * Luego, para cada jugador, calcula el total de victorias, derrotas, empates y puntos.
     * Si el torneo o el usuario no existen, lanza una excepción.
     * 
     * @param tournamentId ID del torneo para el cual se quieren obtener los detalles del ranking.
     * @return Lista de RankingDetailsDTO con los detalles del ranking de los jugadores del torneo.
     */
    public List<RankingDetailsDTO> getRankingDetails(Long tournamentId) {
        // Obtenemos el torneo por su ID de la BD
        Optional<Tournament> tournamentOptional = tournamentRepository.findById(tournamentId);
        
        if(tournamentOptional.isEmpty())
            throw new RequestException(ApiResponse.NOT_FOUND, "Torneo no encontrado", "No se encontro un torneo con esa ID");

        // Obtenemos la lista de jugadores del torneo
        List<User> userList = tournamentOptional.get().getPlayers();

        // Creamos una lista para almacenar los detalles del ranking de cada jugador
        List<RankingDetailsDTO> listDetailsRanking = new ArrayList<>();
        for (User user : userList) {
            // Recoreemos cada usuario del torneo y obtenemos sus detalles
            logger.info("Detalle sde usuario: {}", user.getUsername());

            Optional<User> userOptional = userRepository.findByUsername(user.getUsername());
            if(userOptional.isEmpty())
                throw new RequestException(ApiResponse.NOT_FOUND, "Usuario no encontrado", "No se encontro un usuario con ese nombre");

            User actualUser = userOptional.get();

            // Obtenemos la lista de matches del usuario en esta iteración
            List<Match> listMatches = actualUser.getMatches();

            // Calculamos el total de victorias, derrotas, empates y puntos
            Integer totalWins = 0, totalLosses = 0, totalDraws = 0, totalPoints = 0;
            for (Match match : listMatches) {

                // Asignamos derrotas y victorias según el resultado del match
                Result result = match.getResult();
                boolean isPlayer1 = user.getId().equals(match.getPlayer1().getId());
                boolean isPlayer2 = user.getId().equals(match.getPlayer2().getId());
                if(result == Result.PLAYER1_WIN){
                    totalWins++;
                    totalPoints+=5;
                } else if ((isPlayer1 && result == Result.PLAYER1_WIN) || (isPlayer2 && result == Result.PLAYER2_WIN)) {
                    totalWins++;
                    totalPoints += 10;
                } else if ((isPlayer1 && result == Result.PLAYER2_WIN) || (isPlayer2 && result == Result.PLAYER1_WIN)) {
                    totalLosses++;
                    if (totalPoints > 10) totalPoints -= 10;
                }
            }

            // Creamos un RankingDetailsDTO con los detalles del ranking del usuario y lo añadimos a la lista
            RankingDetailsDTO detailsRankingDTO = new RankingDetailsDTO(user.getUsername(), totalWins, totalLosses, totalDraws, totalPoints);
            listDetailsRanking.add(detailsRankingDTO);
        }
       
        return listDetailsRanking;
    }

    /**
     * Actualiza un torneo existente.
     * Este método busca el torneo por su ID y actualiza sus campos con los valores del
     * TournamentDTO proporcionado.
     * Si el torneo no existe, lanza una excepción.
     * Si el usuario que realiza la solicitud no es un administrador, lanza una excepción.
     * 
     * @param tournamentDTO TournamentDTO que contiene la información del torneo a actualizar.
     * @param authentication Authentication que contiene la información del usuario que realiza la solicitud.
     * @return TournamentDTO con la información del torneo actualizado.
     */
    public TournamentDTO updateTournament(TournamentDTO tournamentDTO, Authentication authentication) {
        Optional<User> userRequest = userRepository.findByUsername(authentication.getName());
        
        if(userRequest.isEmpty() || userRequest.get().getRole() != Role.ADMIN)
        throw new RequestException(ApiResponse.FORBIDDEN, "Acceso denegado", "El usuario no tiene permisos para actualizar torneos");
        
        Optional<Tournament> tournamentOptional = tournamentRepository.findById(tournamentDTO.getId());
        
        if(tournamentOptional.isEmpty())
        throw new RequestException(ApiResponse.NOT_FOUND, "Tournament no encontrado", "No se encontro un torneo con esa ID");
        
        Tournament tournament = tournamentOptional.get();
        this.validateTournament(tournament);

        // Actualizamos los campos del torneo con los valores del DTO
        tournament.setName(tournamentDTO.getName());
        tournament.setMaxPlayers(tournamentDTO.getMaxPlayers());
        tournament.setStatus(tournamentDTO.getStatus());
        tournament.setRounds(tournamentDTO.getRounds());
        tournament.setMaxRounds(tournamentDTO.getMaxRounds());
        tournament.setMatches(tournamentMapper.tournamentDtoToTournament(tournamentDTO).getMatches());
        tournament.setPlayers(tournamentMapper.tournamentDtoToTournament(tournamentDTO).getPlayers());

        // Guardamos el torneo actualizado en la base de datos
        Tournament updatedTournament = tournamentRepository.save(tournament);

        // Devolvemos el TournamentDTO actualizado
        return tournamentMapper.tournamentToTournamentDTO(updatedTournament);
    }

    /**
     * Obtiene una lista con los torneos.
     * Este método busca todos los torneos en la base de datos y los convierte a TournamentDTO.
     * 
     * @param tournamentDTO ID del torneo para el cual se quieren obtener los detalles del ranking.
     * @return Lista de RankingDetailsDTO con los detalles del ranking de los jugadores del torneo.
     */
    public List<TournamentDTO> getAllTournament(){
        List<Tournament> tournaments = tournamentRepository.findAll();

        if(tournaments.isEmpty())
            throw new RequestException(ApiResponse.NOT_FOUND, "No se encontraron torneos", "No se encontraron torneos en la base de datos");

        return tournamentMapper.tournamentListToTournamentDTOList(tournaments);
    }

    /**
     * Actualiza un torneo existente.
     * Este método busca el torneo por su ID y actualiza sus campos con los valores del TournamentDTO proporcionado.
     * Si el torneo no existe, lanza una excepción.
     * 
     * @param tournamentDTO TournamentDTO que contiene la información del torneo a actualizar.
     * @return TournamentDTO con la información del torneo actualizado.
     */
    public TournamentDTO updateTournament(Long id, TournamentDTO tournamentDTO) {
        // Buscamos el torneo por su ID
        Optional<Tournament> tournamentOptional = tournamentRepository.findById(id);

        if(tournamentOptional.isEmpty())
            throw new RequestException(ApiResponse.NOT_FOUND, "Tournament no encontrado", "No se encontro un torneo con esa ID");

        Tournament tournament = tournamentMapper.tournamentDtoToTournament(tournamentDTO);
        tournament.setId(id); // Aseguramos que el ID del torneo es el correcto

        // Actualizamos los campos del torneo con los valores del TournamentDTO
        Tournament updatedTournament = tournamentRepository.save(tournament);
        return tournamentMapper.tournamentToTournamentDTO(updatedTournament);

    }

    /**
     * Elimina un torneo por su ID.
     * Este método busca el torneo por su ID y lo elimina de la base de datos.
     * Si el torneo no existe, lanza una excepción.
     * 
     * @param id ID del torneo a eliminar.
     */
    public void deleteTournament(Long id) {
        // Verificamos si el torneo existe
        Optional<Tournament> tournamentOptional = tournamentRepository.findById(id);
        
        if(tournamentOptional.isEmpty())
            throw new RequestException(ApiResponse.NOT_FOUND, "Tournament no encontrado", "No se encontro un torneo con esa ID");

        // Eliminamos el torneo de la base de datos
        tournamentRepository.deleteById(id);
        logger.info("Torneo con ID {} eliminado correctamente", id);
    }

    /**
     * Valida un torneo para asegurarse de que cumple con las reglas establecidas.
     * Verifica que el número de jugadores no exceda el máximo permitido y que el número
     * @param tournament Torneo a validar.
     */
    public void validateTournament(Tournament tournament) throws RequestException {
         if(tournament.getPlayers().size() > tournament.getMaxPlayers())
            throw new RequestException(ApiResponse.BAD_REQUEST, "Número de jugadores excedido", "El número de jugadores no puede ser mayor que el máximo permitido");
        if(tournament.getRounds() > tournament.getMaxRounds() )
            throw new RequestException(ApiResponse.BAD_REQUEST, "Número de rondas excedido", "El número de rondas no puede ser mayor que el máximo permitido");
    }
}
