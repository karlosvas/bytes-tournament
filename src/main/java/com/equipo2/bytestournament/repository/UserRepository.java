package com.equipo2.bytestournament.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.equipo2.bytestournament.model.User;
 
/**
 * Interfaz que define el repositorio para la entidad Message.
 * Proporciona métodos para buscar usuarios por nombre de usuario y correo electrónico.
 * 
 * {@link Repository} es una anotación de Spring que indica que esta interfaz es un repositorio
 * y será utilizada para acceder a la base de datos.
 * {@link JpaRepository} es una interfaz de Spring Data JPA que proporciona métodos
 * para realizar operaciones CRUD y consultas en la entidad Match.
 * {@link Optional} es una clase de Java que representa un valor que puede estar presente o no,
 * evitando así el uso de null y proporcionando una forma más segura de manejar valores ausentes.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
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
