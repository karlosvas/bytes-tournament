package com.equipo2.bytestournament.mapper;

import com.equipo2.bytestournament.DTO.UserDTO;
import com.equipo2.bytestournament.model.Users;
import org.mapstruct.Mapper;
import java.util.List;

/**
 * Mapper que convierte la entidad User a DTO y la DTO a entidad
 *
 * @author Christian Escalas
 */
@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO userToUserDTO(Users user);

    Users userDtoToUser(UserDTO userDTO);

    List<UserDTO> userToUserDtos(List<Users> users);

    List<Users> userDtosToUsers(List<UserDTO> userDTOS);
}