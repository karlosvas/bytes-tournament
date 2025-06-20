package com.equipo2.bytestournament;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BytestournamentApplication {
	private static final Logger logger = LoggerFactory.getLogger(BytestournamentApplication.class);

	public static void main(String[] args) {
		logger.info("Starting Bytestournament Application");
		SpringApplication.run(BytestournamentApplication.class, args);
	}

}
