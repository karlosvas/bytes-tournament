package com.equipo2.bytestournament;

<<<<<<< Updated upstream
=======
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.springframework.context.ApplicationContext;
>>>>>>> Stashed changes
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BytestournamentApplicationTests {

	private final ApplicationContext applicationContext;

	public BytestournamentApplicationTests(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

<<<<<<< Updated upstream
=======
	@Test
	void contextLoads() {
		 assertNotNull(applicationContext);
	}
>>>>>>> Stashed changes
}
