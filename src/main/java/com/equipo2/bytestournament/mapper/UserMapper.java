package com.equipo2.bytestournament.mapper;

import com.equipo2.bytestournament.DTO.UserDTO;
import com.equipo2.bytestournament.model.User;
import org.mapstruct.Mapper;
import java.util.List;

/**
 * Mapper que convierte la entidad User a DTO y la DTO a entidad
 *
 * @author Christian Escalas
 */
@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO userToUserDTO(User user);

    User userDtoToUser(UserDTO userDTO);

    List<UserDTO> userToUserDtos(List<User> users);

    List<User> userDtosToUsers(List<UserDTO> userDTOS);
}