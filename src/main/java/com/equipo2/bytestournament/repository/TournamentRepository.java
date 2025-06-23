package com.equipo2.bytestournament.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.equipo2.bytestournament.model.Tournament;

/**
 * Interfaz que define el repositorio para la entidad Tournament.
 * 
 * {@link Repository} es una anotación de Spring que indica que esta interfaz es un repositorio
 * y será utilizada para acceder a la base de datos.
 * {@link JpaRepository} es una interfaz de Spring Data JPA que proporciona métodos
 * para realizar operaciones CRUD y consultas en la entidad Match.
 */
@Repository
public interface TournamentRepository extends JpaRepository  <Tournament, Long>{
}
