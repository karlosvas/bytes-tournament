package com.equipo2.bytestournament.mapper;

import com.equipo2.bytestournament.DTO.MatchDTO;
import com.equipo2.bytestournament.enums.Result;
import com.equipo2.bytestournament.mapper.helper.MatchMapperHelper;
import com.equipo2.bytestournament.model.Match;
import com.equipo2.bytestournament.model.Tournament;
import com.equipo2.bytestournament.model.User;
import com.equipo2.bytestournament.repository.TournamentRepository;
import com.equipo2.bytestournament.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

class MatchMapperTest {

    /**
     * matchMapper Instancia del MatchMapper que se va a probar.
     * tournamentRepository Repositorio de torneos, usado por el MatchMapperHelper.
     * userRepository Repositorio de usuarios, usado por el MatchMapperHelper.
     */
    private static MatchMapper matchMapper;

    @Mock
    private static TournamentRepository tournamentRepository;
    
    @Mock
    private static UserRepository userRepository;

   /**
    * setUp Método que se ejecuta antes de cada prueba.
    * Inicializa el MatchMapper y su helper, inyectando los repositorios necesarios.
    */
    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        matchMapper = Mappers.getMapper(MatchMapper.class);
        Field helperField = matchMapper.getClass().getDeclaredField("matchMapperHelper");
        MatchMapperHelper helper = new MatchMapperHelper(userRepository, tournamentRepository);
        helperField.setAccessible(true);
        helperField.set(matchMapper, helper);
    }

    /**
     * testMatchToMatchDTOAndBack Método que prueba la conversión
     * de Match a MatchDTO y viceversa.
     * Crea un Match, lo convierte a MatchDTO y luego de vuelta a Match.
     * Verifica que los campos coincidan en ambas conversiones.
     */
    @Test
    void testMatchToMatchDTOAndBack() {
        Tournament tournament = Tournament.builder().id(1L).build();
        User player1 = User.builder().id(10L).build();
        User player2 = User.builder().id(20L).build();

        Match match = Match.builder()
                .id(100L)
                .tournament(tournament)
                .player1(player1)
                .player2(player2)
                .result(Result.PENDING)
                .round(1)
                .build();

        MatchDTO dto = matchMapper.matchToMatchDTO(match);
        assertNotNull(dto);
        assertAll(
            () -> assertEquals(match.getId(), dto.getId()),
            () -> assertEquals(match.getTournament().getId(), dto.getTournament()),
            () -> assertEquals(match.getPlayer1().getId(), dto.getPlayer1()),
            () -> assertEquals(match.getPlayer2().getId(), dto.getPlayer2()),
            () -> assertEquals(match.getResult(), dto.getResult()),
            () -> assertEquals(match.getRound(), dto.getRound())
        );

        // Mockea el comportamiento del repositorio
        Mockito.when(userRepository.findById(10L)).thenReturn(java.util.Optional.of(player1));
        Mockito.when(userRepository.findById(20L)).thenReturn(java.util.Optional.of(player2));
        Mockito.when(tournamentRepository.findById(1L)).thenReturn(java.util.Optional.of(tournament));
        Match matchMapped = matchMapper.matchDtoToMatch(dto);
        assertNotNull(matchMapped);
        assertAll(
            () -> assertEquals(match.getId(), matchMapped.getId()),
            () -> assertEquals(match.getTournament().getId(), matchMapped.getTournament().getId()),
            () -> assertEquals(match.getPlayer1().getId(), matchMapped.getPlayer1().getId()),
            () -> assertEquals(match.getPlayer2().getId(), matchMapped.getPlayer2().getId()),
            () -> assertEquals(match.getResult(), matchMapped.getResult()),
            () -> assertEquals(match.getRound(), matchMapped.getRound())
        );
    }

    /**
     * testMatchListToMatchDTOList Método que prueba la conversión
     * de una lista de Match a una lista de MatchDTO.
     * Crea una lista de Match, la convierte a MatchDTO y verifica que
     * la lista resultante tenga el tamaño correcto y los IDs coincidan.
     */
    @Test
    void testMatchListToMatchDTOList() {
        Tournament tournament = Tournament.builder().id(1L).build();
        User player1 = User.builder().id(10L).build();
        User player2 = User.builder().id(20L).build();

        Match match = Match.builder()
                .id(100L)
                .tournament(tournament)
                .player1(player1)
                .player2(player2)
                .result(Result.PLAYER1_WIN)
                .round(2)
                .build();

        List<MatchDTO> dtoList = matchMapper.matchListToMatchDTOList(List.of(match));
        assertNotNull(dtoList);
        assertEquals(1, dtoList.size());
        assertAll(
            () -> assertEquals(match.getId(), dtoList.get(0).getId()),
            () -> assertEquals(match.getTournament().getId(), dtoList.get(0).getTournament()),
            () -> assertEquals(match.getPlayer1().getId(), dtoList.get(0).getPlayer1()),
            () -> assertEquals(match.getPlayer2().getId(), dtoList.get(0).getPlayer2()),
            () -> assertEquals(match.getResult(), dtoList.get(0).getResult()),
            () -> assertEquals(match.getRound(), dtoList.get(0).getRound())  
        );
    }

    /**
     * testMatchDTOListToMatchList Método que prueba la conversión
     * de una lista de MatchDTO a una lista de Match.
     * Crea una lista de MatchDTO, la convierte a Match y verifica que
     * la lista resultante tenga el tamaño correcto y los IDs coincidan.
     */
    @Test
    void testMatchDTOListToMatchList() {
        Tournament tournament = Tournament.builder().id(1L).build();
        User player1 = User.builder().id(1L).build();
        User player2 = User.builder().id(2L).build();

        MatchDTO dto = MatchDTO.builder()
                .id(200L)
                .tournament(1L)
                .player1(1L)
                .player2(2L)
                .result(Result.PLAYER2_WIN)
                .round(3)
                .build();

        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(player1));
        Mockito.when(userRepository.findById(2L)).thenReturn(Optional.of(player2));
        Mockito.when(tournamentRepository.findById(1L)).thenReturn(Optional.of(tournament));

        List<Match> matchList = matchMapper.matchDTOListToMatchList(List.of(dto));
        assertNotNull(matchList);
        assertEquals(1, matchList.size());
        assertAll(
            () -> assertEquals(dto.getId(), matchList.get(0).getId()),
            () -> assertEquals(dto.getTournament(), matchList.get(0).getTournament().getId()),
            () -> assertEquals(dto.getPlayer1(), matchList.get(0).getPlayer1().getId()),
            () -> assertEquals(dto.getPlayer2(), matchList.get(0).getPlayer2().getId()),
            () -> assertEquals(dto.getResult(), matchList.get(0).getResult()),
            () -> assertEquals(dto.getRound(), matchList.get(0).getRound())
       );
    }
}