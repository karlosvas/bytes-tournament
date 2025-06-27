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
import org.mockito.MockitoAnnotations;
import java.lang.reflect.Field;
import java.util.List;

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
     * Este método inyecta el MatchMapperHelper en el MatchMapper.
     * Inyecta el MatchMapperHelper en el MatchMapper.
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
        assertEquals(match.getId(), dto.getId());
        assertEquals(match.getTournament().getId(), dto.getTournament());
        assertEquals(match.getPlayer1().getId(), dto.getPlayer1());
        assertEquals(match.getPlayer2().getId(), dto.getPlayer2());
        assertEquals(match.getResult(), dto.getResult());
        assertEquals(match.getRound(), dto.getRound());

        Match matchMapped = matchMapper.matchDtoToMatch(dto);
        assertNotNull(matchMapped);
        assertEquals(dto.getId(), matchMapped.getId());
        assertEquals(dto.getResult(), matchMapped.getResult());
        assertEquals(dto.getRound(), matchMapped.getRound());
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
        assertEquals(match.getId(), dtoList.get(0).getId());
    }

    /**
     * testMatchDTOListToMatchList Método que prueba la conversión
     * de una lista de MatchDTO a una lista de Match.
     * Crea una lista de MatchDTO, la convierte a Match y verifica que
     * la lista resultante tenga el tamaño correcto y los IDs coincidan.
     */
    @Test
    void testMatchDTOListToMatchList() {
        MatchDTO dto = MatchDTO.builder()
                .id(200L)
                .tournament(2L)
                .player1(11L)
                .player2(22L)
                .result(Result.PLAYER2_WIN)
                .round(3)
                .build();

        List<Match> matchList = matchMapper.matchDTOListToMatchList(List.of(dto));
        assertNotNull(matchList);
        assertEquals(1, matchList.size());
        assertEquals(dto.getId(), matchList.get(0).getId());
    }
}