package com.equipo2.bytestournament.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.equipo2.bytestournament.model.User;
 
@Repository
public interface UserRepository extends JpaRepository  <User, Long> {

    /**
     * Busca un usuario por su nombre de usuario.
     *
     * @param username el nombre de usuario a buscar
     * @return un Optional con el usuario si existe, o vacío si no
     */
    Optional<User> findByUsername(String username);

    /**
     * Busca un usuario por su correo electrónico.
     *
     * @param email el correo electrónico a buscar
     * @return un Optional con el usuario si existe, o vacío si no
     */
    Optional<User> findByEmail(String email);
}
