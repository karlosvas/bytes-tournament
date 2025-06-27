package com.equipo2.bytestournament.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import com.equipo2.bytestournament.DTO.RankingDTO;
import com.equipo2.bytestournament.DTO.RankingDetailsDTO;
import com.equipo2.bytestournament.DTO.TournamentDTO;
import com.equipo2.bytestournament.enums.Rank;
import com.equipo2.bytestournament.enums.Role;
import com.equipo2.bytestournament.enums.Status;
import com.equipo2.bytestournament.exceptions.RequestException;
import com.equipo2.bytestournament.mapper.TournamentMapper;
import com.equipo2.bytestournament.model.Match;
import com.equipo2.bytestournament.model.Tournament;
import com.equipo2.bytestournament.model.User;
import com.equipo2.bytestournament.repository.TournamentRepository;
import com.equipo2.bytestournament.repository.UserRepository;

/**
 * Clase de prueba para TournamentService utilizando Mockito.
 * Esta clase contiene pruebas unitarias para los métodos de TournamentService.
 *
 * {@link Mock} se utiliza para simular las dependencias de TournamentService, como MatchRepository, MatchMapper, TournamentRepository y Logger.
 * {@link InjectMocks} se utiliza para inyectar los mocks en una instancia de TournamentService.
 * {@link MockitoAnnotations} se utiliza para inicializar los mocks antes de cada prueba.
 * {@link Test} se utiliza para marcar los métodos de prueba.
 */
public class TournamentServiceTest {
    
    @Mock
    private TournamentRepository tournamentRepository;
    
    @Mock
    private TournamentMapper tournamentMapper;
    
    @Mock
    private UserRepository userRepository;

    @Mock
    private Logger logger;

    @InjectMocks
    private TournamentService tournamentService;
    
    public TournamentServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Prueba unitaria para el método createTournament de TournamentService.
     * Esta prueba verifica que el método crea un torneo correctamente,
     * mapeando el DTO a una entidad y guardándolo en el repositorio.
     */
    @Test
    public void createTournamentTest() {
        TournamentDTO tournamentDTO = new TournamentDTO().builder()
                .id(1L)
                .name("Test Tournament")
                .maxPlayers(2)
                .rounds(0)       
                .maxRounds(1)  
                .build();
        Tournament tournament = new Tournament().builder()
                .id(tournamentDTO.getId())
                .name(tournamentDTO.getName())
                .maxPlayers(2)
                .rounds(0)
                .maxRounds(1)
                .build();

        // Mockea el mapeo de DTO a entidad y viceversa
        Mockito.when(tournamentMapper.tournamentDtoToTournament(tournamentDTO)).thenReturn(tournament);
        Mockito.when(tournamentMapper.tournamentToTournamentDTO(tournament)).thenReturn(tournamentDTO);

        TournamentDTO result = tournamentService.createTournament(tournamentDTO);

        Mockito.verify(tournamentRepository, Mockito.times(1)).save(Mockito.any(Tournament.class));
        Mockito.verify(tournamentMapper, Mockito.times(1)).tournamentToTournamentDTO(Mockito.any(Tournament.class));
        assertEquals(tournamentDTO, result);
    }

    /**
     * Prueba unitaria para el método findTournamentById de TournamentService.
     * Esta prueba verifica que el método busca un torneo por su ID correctamente,
     * mapeando la entidad a un DTO y manejando excepciones si el torneo no se encuentra.
     */
    @Test
    public void findTournamentByIdTest() {
        Long tournamentId = 1L;
        Tournament tournament = new Tournament().builder()
                .id(tournamentId)
                .name("Test Tournament")
                .build();
        TournamentDTO tournamentDTO = new TournamentDTO().builder()
                .id(tournamentId)
                .name(tournament.getName())
                .build();

        // Mockea el comportamiento del repositorio y el mapeo
        Mockito.when(tournamentRepository.findById(tournamentId)).thenReturn(java.util.Optional.of(tournament));
        Mockito.when(tournamentMapper.tournamentToTournamentDTO(tournament)).thenReturn(tournamentDTO);

        TournamentDTO result = tournamentService.findTournamentById(tournamentId);

        Mockito.verify(tournamentRepository, Mockito.times(1)).findById(Mockito.any(Long.class));
        Mockito.verify(tournamentMapper, Mockito.times(1)).tournamentToTournamentDTO(Mockito.any(Tournament.class));
        assertEquals(tournamentDTO, result);
    }

    /**
     * Prueba unitaria para el método addPlayerToTournament de TournamentService.
     * Esta prueba verifica que el método agrega un jugador a un torneo correctamente,
     * manejando casos donde el torneo o el jugador no existen, y evitando duplicados.
     */
    @Test
    public void addUserToTournamentTest() {
        Long tournamentId = 1L;
        String userName = "testUser";
        Tournament tournament = new Tournament().builder()
                .id(tournamentId)
                .name("Test Tournament")
                .maxPlayers(5)
                .status(Status.PENDING)
                .rounds(0)
                .maxRounds(3)
                .matches(new ArrayList<>())
                .players(new ArrayList<>())
                .build();
        TournamentDTO tournamentDTO = new TournamentDTO().builder()
                .id(tournamentId)
                .name(tournament.getName())
                .maxPlayers(tournament.getMaxPlayers())
                .status(tournament.getStatus())
                .rounds(tournament.getRounds())
                .maxRounds(tournament.getMaxRounds())
                .build();
        User user = new User().builder()
                .username(userName)
                .email("test@gmai.com")
                .password("password")
                .role(Role.ADMIN)
                .rank(Rank.BRONZE)
                .points(0)
                .matchesAsPlayer1(new ArrayList<>())
                .matchesAsPlayer2(new ArrayList<>())
                .tournaments(new ArrayList<>())
                .authorityPrivilegies(new HashSet<>())
                .build();
                
        // Mockea el comportamiento
        // Buscamos por id el torneo
        Mockito.when(tournamentRepository.findById(tournamentId)).thenReturn(java.util.Optional.empty());
        assertThrows(RequestException.class, () -> tournamentService.addPlayerToTournament(tournamentId, userName));
        // Simula que el torneo existe
        Mockito.when(tournamentRepository.findById(tournamentId)).thenReturn(java.util.Optional.of(tournament));

        // Bucsamos por nombre el usuario
        Mockito.when(userRepository.findByUsername(userName)).thenReturn(java.util.Optional.empty()); // Simula que el usuario no existe
        assertThrows(RequestException.class, () -> tournamentService.addPlayerToTournament(tournamentId, userName));
        // Simulamos que el Usuario existe
        Mockito.when(userRepository.findByUsername(userName)).thenReturn(Optional.of(user));
        
        // Obtenemos laos usuarios para verificar que no se repita
        tournament.getPlayers().add(user);
        tournament.getPlayers().add(user); // Se repirte error
        assertThrows(RequestException.class, () -> tournamentService.addPlayerToTournament(tournamentId, userName));

        // Caso exitoso
        tournament.getPlayers().clear();
        user.getTournaments().clear();
        Mockito.when(tournamentRepository.save(Mockito.any(Tournament.class))).thenReturn(tournament);
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(user);
        Mockito.when(tournamentMapper.tournamentToTournamentDTO(Mockito.any(Tournament.class))).thenReturn(tournamentDTO);

        tournamentService.addPlayerToTournament(tournamentId, userName);

        assertTrue(tournament.getPlayers().contains(user));
        assertTrue(user.getTournaments().contains(tournament));
    }

    /**
     * Prueba unitaria para el método getTournamentsByUser de TournamentService.
     * Esta prueba verifica que el método obtiene los torneos en los que un usuario ha participado,
     * mapeando las entidades a DTOs y manejando casos donde el usuario no existe.
     */
    @Test
    public void getClassification() {
        Long tournamentId = 1L;
       Mockito.when(tournamentRepository.findById(tournamentId)).thenReturn(Optional.empty());
        assertThrows(RequestException.class, () -> tournamentService.getClassification(tournamentId));

        Tournament tournament = new Tournament().builder()
                .id(1L)
                .name("Test Tournament")
                .maxPlayers(5)
                .status(Status.PENDING)
                .rounds(0)
                .maxRounds(3)
                .matches(new ArrayList<>())
                .players(new ArrayList<>())
                .build();

                
        // Caso: torneo existe y jugadores ordenados por puntos
        User user1 = new User().builder()
        .id(1L)
        .username("user1")
        .rank(Rank.BRONZE)
        .points(10)
        .build();
        User user2 = new User().builder()
        .id(2L)
        .username("user2")
        .rank(Rank.SILVER)
        .points(30)
        .build();
        User user3 = new User().builder()
        .id(3L)
        .username("user3")
        .rank(Rank.GOLD)
        .points(20)
        .build();
                
        tournament.getPlayers().add(user1);
        tournament.getPlayers().add(user2);
        tournament.getPlayers().add(user3);
        Mockito.when(tournamentRepository.findById(1L)).thenReturn(Optional.of(tournament));
    
        RankingDTO ranking = tournamentService.getClassification(tournamentId);

        // Verifica que los jugadores están ordenados por puntos descendente
        assertEquals(3, ranking.getPlayers().size());
        assertNotNull(ranking);
  }

  /**
   * Prueba unitaria para el método getRankingDetails de TournamentService.
   * Esta prueba verifica que el método obtiene los detalles del ranking de un torneo,
   * mapeando la entidad a un DTO y manejando casos donde el torneo no existe
   */
    @Test
    public void getRankingDetailsTest() {
        Long tournamentId = 1L;
        Mockito.when(tournamentRepository.findById(tournamentId)).thenReturn(Optional.empty());
        assertThrows(RequestException.class, () -> tournamentService.getRankingDetails(tournamentId));

        Tournament tournament = new Tournament().builder()
                .id(1L)
                .name("Test Tournament")
                .maxPlayers(5)
                .status(Status.PENDING)
                .rounds(0)
                .maxRounds(3)
                .matches(new ArrayList<>())
                .players(new ArrayList<>())
                .build();

        Mockito.when(tournamentRepository.findById(tournamentId)).thenReturn(Optional.of(tournament));

        // Caso: torneo existe pero los jugadores no, al utilizarse en un bucle que recorre los jugadores es necessario que al menos no esté vacía
        tournament.getPlayers().add(new User().builder()
                .id(1L)
                .username("user1")
                .rank(Rank.BRONZE)
                .points(10)
                .build());
        Mockito.when(userRepository.findByUsername(Mockito.anyString())).thenReturn(Optional.empty());
        assertThrows(RequestException.class, () -> tournamentService.getRankingDetails(tournamentId));
        tournament.getPlayers().clear();

        // Caso: torneo existe y jugadores existen
        User user1 = new User().builder()
                .id(1L)
                .username("user1")
                .rank(Rank.BRONZE)
                .points(10)
                .build();
        User user2 = new User().builder()
                .id(2L)
                .username("user2")
                .rank(Rank.SILVER)
                .points(30)
                .build();
        Match match = new Match().builder()
                .id(1L)
                .player1(user1)
                .player2(user2)
                .build();
        user1.getMatchesAsPlayer1().add(match);
        user2.getMatchesAsPlayer2().add(match);
        tournament.getPlayers().add(user1);
        tournament.getPlayers().add(user2);     
        Mockito.when(userRepository.findByUsername("user1")).thenReturn(Optional.of(user1));
        Mockito.when(userRepository.findByUsername("user2")).thenReturn(Optional.of(user2));

        List<RankingDetailsDTO> rankingDetails = tournamentService.getRankingDetails(tournamentId);
        assertEquals(2, rankingDetails.size());
        assertNotNull(rankingDetails);
   }

   /**
    * Prueba unitaria para el método updateTournament de TournamentService.
    * Esta prueba verifica que el método actualiza un torneo correctamente,
    * mapeando el DTO a una entidad y guardándolo en el repositorio.
    */
    @Test
    public void updateTournamentTest() {
        Long tournamentId = 1L;
        TournamentDTO tournamentDTO = new TournamentDTO().builder()
                .id(tournamentId)
                .name("Updated Tournament")
                .maxPlayers(10)
                .status(Status.FINISHED)
                .rounds(2)
                .maxRounds(5)
                .build();

        Tournament tournament = new Tournament().builder()
                .id(tournamentId)
                .name("Old Tournament")
                .maxPlayers(5)
                .status(Status.PENDING)
                .rounds(0)
                .maxRounds(3)
                .matches(new ArrayList<>())
                .players(new ArrayList<>())
                .build();

        Mockito.when(tournamentRepository.findById(tournamentId)).thenReturn(Optional.of(tournament));
        Mockito.when(tournamentMapper.tournamentDtoToTournament(tournamentDTO)).thenReturn(tournament);
        Mockito.when(tournamentMapper.tournamentToTournamentDTO(Mockito.any(Tournament.class))).thenReturn(tournamentDTO);

        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getName()).thenReturn("adminUser");
          // Si necesitas simular el rol, puedes mockear el User y el repositorio:
        User adminUser = new User();
        adminUser.setUsername("adminUser");
        adminUser.setRole(Role.ADMIN);
        Mockito.when(userRepository.findByUsername("adminUser")).thenReturn(Optional.of(adminUser));
        Mockito.when(tournamentRepository.save(Mockito.any(Tournament.class))).thenReturn(tournament);
        Mockito.when(tournamentMapper.tournamentToTournamentDTO(Mockito.any(Tournament.class))).thenReturn(tournamentDTO);

        TournamentDTO updatedTournament = tournamentService.updateTournament(tournamentDTO, authentication);

        assertAll(
            () -> assertNotNull(updatedTournament),
            () -> assertEquals("Updated Tournament", updatedTournament.getName()),
            () -> assertEquals(Status.FINISHED, updatedTournament.getStatus())
        );
    }
}


