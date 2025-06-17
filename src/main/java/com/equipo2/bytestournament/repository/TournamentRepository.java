package com.equipo2.bytestournament.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.equipo2.bytestournament.model.Tournament;

@Repository
public interface TournamentRepository extends JpaRepository  <Tournament, Long>{

}
