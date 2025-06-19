package com.equipo2.bytestournament.mapper;

import com.equipo2.bytestournament.DTO.UserDTO;
import com.equipo2.bytestournament.model.User;
import com.equipo2.bytestournament.enums.Role;
import com.equipo2.bytestournament.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.ArrayList;

/**
 * Mapper que convierte la entidad User a DTO y la DTO a entidad
 *
 * @author Christian Escalas
 */
@Component
public class UserMapper {

    private final UserRepository userRepository;

    /**
     * Constructor para inyectar las dependencias.
     *
     * @param userRepository repositorio de usuarios.
     */
    public UserMapper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Convierte un UserDTO en una instancia de User.
     * <p>
     * Si userDTO es null, devuelve null.
     *
     * @param userDTO con los datos de la partida.
     * @return una entidad User con los campos asignados desde el DTO.
     */
    public User userDtoToUser(UserDTO userDTO) {

        if (userDTO == null) {
            return null;
        }

        User user = new User();

        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setRole(userDTO.getRole());
        user.setRank(userDTO.getRank());
        user.setPoints(userDTO.getPoints());

        return user;
    }

    /**
     * Convierte una entidad User en UserDTO.
     * <p>
     * Si user es null, devuelve null.
     *
     * @param user entidad que hay que convertir a DTO.
     * @return DTO con los mismos valores que la entidad.
     */
    public UserDTO userToUserDTO(User user) {

        if (user == null) {
            return null;
        }

        UserDTO userDTO = new UserDTO();

        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(user.getPassword());
        userDTO.setRole(user.getRole());
        userDTO.setRank(user.getRank());
        userDTO.setPoints(user.getPoints());

        return userDTO;
    }

    /**
     * Convierte una lista de entidades User a una lista de UserDTO.
     * <p>
     * Si users es null, devuelve una lista vacía.
     * Cada elemento user de la lista se convierte a UserDTO.
     *
     * @param users lista de entidades User. Puede ser null.
     * @return lista de DTOs, pero si la lista users está vacía devuelve una lista vacía.
     */
    public List<UserDTO> userToUserDtos(List<User> users) {

        if (users == null) {
            return List.of();
        }

        List<UserDTO> userDtos = new ArrayList<>();
        for (User user : users) {
            userDtos.add(userToUserDTO(user));
        }
        return userDtos;
    }

    /**
     * Actualiza los campos de la entidad User.
     * <p>
     * Si userDTO es null o user es null, no hace nada.
     *
     * @param user    entidad que se va a actualizar. Si es null, no hace nada.
     * @param userDTO con los nuevos valores. Si es null, no hace nada.
     */
    public void updateUser(User user, UserDTO userDTO) {

        if (user == null || userDTO == null) {
            return;
        } else {
            user.setUsername(userDTO.getUsername());
            user.setEmail(userDTO.getEmail());
            user.setPassword(userDTO.getPassword());
            user.setRole(userDTO.getRole());
            user.setRank(userDTO.getRank());
            user.setPoints(userDTO.getPoints());
        }
    }
}