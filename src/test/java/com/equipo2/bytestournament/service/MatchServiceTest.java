package com.equipo2.bytestournament.service;

import com.equipo2.bytestournament.DTO.MatchDTO;
import com.equipo2.bytestournament.enums.Result;
import com.equipo2.bytestournament.exceptions.RequestException;
import com.equipo2.bytestournament.mapper.MatchMapper;
import com.equipo2.bytestournament.model.Match;
import com.equipo2.bytestournament.model.Tournament;
import com.equipo2.bytestournament.model.User;
import com.equipo2.bytestournament.repository.MatchRepository;
import com.equipo2.bytestournament.repository.TournamentRepository;
import com.equipo2.bytestournament.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Clase de prueba para UserService utilizando Mockito.
 * Esta clase contiene pruebas unitarias para los métodos de registro, inicio de sesión y obtención de datos del perfil de usuario.
 *
 * {}@link Mock} se utiliza para simular las dependencias de UserService, como AuthenticationManager, UserRepository, UserMapper, JwtUtil y PasswordEncoder.
 * {@link InjectMocks} se utiliza para inyectar los mocks en una instancia de UserService.
 * {@link MockitoAnnotations} se utiliza para inicializar los mocks antes de cada prueba.
 * {@link Test} se utiliza para marcar los métodos de prueba.
 */
public class MatchServiceTest {

    @Mock
    private MatchRepository matchRepository;

    @Mock
    private MatchMapper matchMapper;

    @Mock
    private TournamentRepository tournamentRepository;

    @Mock
    private Logger logger;

    @Mock
    private UserRepository userRepository;

    private final User user;

    @InjectMocks
    private MatchService matchService;

    public MatchServiceTest () {
        MockitoAnnotations.openMocks(this);
        user = new User().builder()
                .username("testUser")
                .email("test@gmial.com")
                .points(100)
                .build();
    }

    @Test
    public void generateMatchesTest() {
        // Simulación si no exixste el torneo
        // Comprobamos que se lanza la excepcion
        Mockito.when(tournamentRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(RequestException.class, () -> matchService.generateMatches(2L));

        // Simulación si el torneo existe pero no tiene jugadores
        Tournament tournamentError = new Tournament();
        Mockito.when(tournamentRepository.findById(3L)).thenReturn(Optional.of(tournamentError));
        assertThrows(RequestException.class, () -> matchService.generateMatches(3L));

       // Caso exitoso
        List<User> players = new ArrayList<>();
        User user1 = User.builder().username("a").email("a@a.com").points(100).build();
        User user2 = User.builder().username("b").email("b@b.com").points(110).build();
        
        players.add(user1);
        players.add(user2);

        Tournament tournament = Tournament.builder()
                .id(1L)
                .name("Torneo de prueba")
                .rounds(0)
                .players(players)
                .matches(new ArrayList<>())
                .build();

        // Simulamos el comportamiento del repositorio y el mapper
        Mockito.when(tournamentRepository.findById(1L)).thenReturn(Optional.of(tournament));
        Mockito.when(matchRepository.save(Mockito.any(Match.class))).thenAnswer(i -> i.getArgument(0));
        Mockito.when(tournamentRepository.save(Mockito.any(Tournament.class))).thenReturn(tournament);
        List<MatchDTO> matchDTOList = new ArrayList<>();
        Mockito.when(matchMapper.matchListToMatchDTOList(Mockito.anyList())).thenReturn(matchDTOList);
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenAnswer(i -> i.getArgument(0));
        
        // // Llama al método a probar
        List<MatchDTO> resultado = matchService.generateMatches(1L);

        // Verifica el resultado
        Mockito.verify(matchRepository, Mockito.atLeastOnce()).save(Mockito.any(Match.class));
        Mockito.verify(tournamentRepository).save(tournament);
        Mockito.verify(matchMapper).matchListToMatchDTOList(tournament.getMatches());
        assertEquals(matchDTOList, resultado);
    }

    /**
     * Prueba unitaria para el método matchUsers de MatchService.
     * Esta prueba verifica el comportamiento del método al emparejar usuarios basándose en sus puntos.
     * Se simulan diferentes escenarios, como cuando hay menos de 2 jugadores y cuando hay
     */
    @Test
    public void matchUsersTest() {
         // Caso: menos de 2 jugadores
        List<User> soloUno = new ArrayList<>();
        soloUno.add(this.user);
        assertThrows(RequestException.class, () -> matchService.matchUsers(soloUno));

        // Caso: 2 jugadores, diferencia dentro del umbral
        List<User> dosJugadores = new ArrayList<>();
        User user1 = this.user;
        User user2 = new User().builder().username("b").email("b@b.com").points(110).build();
        dosJugadores.add(user1);
        dosJugadores.add(user2);

        // La longitud deve de ser exactamente 2
        List<User> emparejados = matchService.matchUsers(dosJugadores);
        assertEquals(2, emparejados.size());

        // Los jugadores emparejados deben ser los que estaban en la lista original
        assertTrue(emparejados.contains(user1));
        assertTrue(emparejados.contains(user2));

        // La diferencia de puntos debe estar dentro del umbral
        assertTrue(Math.abs(user1.getPoints() - user2.getPoints()) <= Match.UMBRAL);
    }

    /**
     * Prueba unitaria para el método checkMatch de MatchService.
     * Esta prueba verifica el comportamiento del método al comprobar la existencia de un match.
     */
    @Test
    public void checkMatchTest() {
        // Caso: el match no existe
        Mockito.when(matchRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RequestException.class, () -> matchService.checkMatch(1L));

        User user1 = new User().builder().id(1L).username("Player1").email("a@gmail.com").points(100).build();
        User user2 = new User().builder().id(2L).username("Player2").email("b@gmail.com").points(110).build();

        // Caso: el match existe
        // Simulamos el comportamiento del repositorio y el mapper
        MatchDTO matchDTOMock = new MatchDTO().builder()
                .id(2L)
                .player1(user1.getId())
                .player2(user2.getId())
                .result(Result.PENDING)
                .round(0)
                .build();

        Match matchMock = new Match().builder()
                .id(2L)
                .player1(user1)
                .player2(user2)
                .result(Result.PENDING)
                .round(0)
                .build();
        
        Mockito.when(matchRepository.findById(2L)).thenReturn(Optional.of(matchMock));
        Mockito.when(matchMapper.matchToMatchDTO(Mockito.any(Match.class))).thenReturn(matchDTOMock);

        // Llama al método a probar
        MatchDTO matchDTO = matchService.checkMatch(2L);
        Mockito.verify(matchRepository, Mockito.times(1)).findById(2L);
        Mockito.verify(matchMapper, Mockito.times(1)).matchToMatchDTO(Mockito.any(Match.class));
        // Verifica que el matchDTO devuelto sea el esperado
        assertEquals(matchDTOMock, matchDTO);        
    }

    /**
     * Prueba unitaria para el método updateMatchResult de MatchService.
     * Esta prueba verifica el comportamiento del método al actualizar el resultado de un match.
     */
    @Test
    public void updateMatchResultTest() {
        MatchDTO matchDTO = new MatchDTO().builder()
                .id(1L)
                .tournament(1L)
                .player1(1L)
                .player2(2L)
                .result(Result.PENDING)
                .round(0)
                .build();

        // Caso: el match no existe
        Mockito.when(matchRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RequestException.class, () -> matchService.updateMatchResult(1L, matchDTO));
 
        // Caso: el match existe y los jugadores son parte del match
        Match existingMatch = Match.builder()
                .id(3L)
                .tournament(Tournament.builder().id(1L).build())
                .player1(User.builder().id(1L).points(100).build())
                .player2(User.builder().id(2L).points(100).build())
                .result(Result.PLAYER1_WIN)
                .round(0)
                .build();

        // Simulamos el comportamiento del repositorio, obtiene el match
        Mockito.when(matchRepository.findById(3L)).thenReturn(Optional.of(existingMatch));
        // Simulamos el comportamiento del mapper
        Mockito.when(matchMapper.matchDtoToMatch(Mockito.any(MatchDTO.class))).thenReturn(existingMatch);

        // Lo convertimos a DTO
        Mockito.when(matchMapper.matchToMatchDTO(Mockito.any(Match.class))).thenReturn(matchDTO);

        // Llama al método a probar
        MatchDTO result = matchService.updateMatchResult(3L, matchDTO);

        // Verifica que se haya guardado el resultado correctamente
        Mockito.verify(matchRepository, Mockito.times(1)).save(Mockito.any(Match.class));
        Mockito.verify(matchMapper, Mockito.times(1)).matchToMatchDTO(Mockito.any(Match.class));

        // Verifica que el resultado sea el esperado
        assertAll(
            () -> assertEquals(Result.PENDING, result.getResult()),
            () -> assertEquals(1L, result.getId()),
            () -> assertEquals(1L, result.getPlayer1()),
            () -> assertEquals(2L, result.getPlayer2()),
            () -> assertEquals(0, result.getRound()),
            () -> assertEquals(1L, result.getTournament())
        );
    }
}
