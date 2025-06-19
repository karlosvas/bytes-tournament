package com.equipo2.bytestournament;

<<<<<<< HEAD
<<<<<<< Updated upstream
=======
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.springframework.context.ApplicationContext;
>>>>>>> Stashed changes
=======
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.BeforeEach;
>>>>>>> d4e2a72 (feat: add SwaggerUI config and improve author privilegies)
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import com.equipo2.bytestournament.repository.TournamentRepository;

@SpringBootTest
class BytestournamentApplicationTests {

	private final ApplicationContext applicationContext;

	public BytestournamentApplicationTests(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

<<<<<<< HEAD
<<<<<<< Updated upstream
=======
	@Test
	void contextLoads() {
		 assertNotNull(applicationContext);
	}
>>>>>>> Stashed changes
=======
	@Mock
    private TournamentRepository tournamentRepository;
    
    @InjectMocks
    private TournamentService tournamentService;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    void testGetAllTournaments() {
        // Arrange
        Tournaments tournament1 = new Tournaments();
        tournament1.setId(1L);
        tournament1.setName("Torneo 1");
        
        Tournaments tournament2 = new Tournaments();
        tournament2.setId(2L);
        tournament2.setName("Torneo 2");
        
        when(tournamentRepository.findAll()).thenReturn(Arrays.asList(tournament1, tournament2));
        
        // Act
        List<Tournaments> result = tournamentService.getAllTournaments();
        
        // Assert
        assertEquals(2, result.size());
        assertEquals("Torneo 1", result.get(0).getName());
        assertEquals("Torneo 2", result.get(1).getName());
        verify(tournamentRepository, times(1)).findAll();
    }
    
    @Test
    void testGetTournamentById_ExistingId() {
        // Arrange
        Tournaments tournament = new Tournaments();
        tournament.setId(1L);
        tournament.setName("Torneo 1");
        
        when(tournamentRepository.findById(1L)).thenReturn(Optional.of(tournament));
        
        // Act
        Optional<Tournaments> result = tournamentService.getTournamentById(1L);
        
        // Assert
        assertTrue(result.isPresent());
        assertEquals("Torneo 1", result.get().getName());
        verify(tournamentRepository, times(1)).findById(1L);
    }
    
    @Test
    void testGetTournamentById_NonExistingId() {
        // Arrange
        when(tournamentRepository.findById(99L)).thenReturn(Optional.empty());
        
        // Act
        Optional<Tournaments> result = tournamentService.getTournamentById(99L);
        
        // Assert
        assertFalse(result.isPresent());
        verify(tournamentRepository, times(1)).findById(99L);
    }
>>>>>>> d4e2a72 (feat: add SwaggerUI config and improve author privilegies)
}
