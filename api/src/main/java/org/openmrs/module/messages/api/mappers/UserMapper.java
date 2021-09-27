package org.openmrs.module.messages.api.mappers;

import org.openmrs.User;
import org.openmrs.module.messages.api.dto.UserDTO;

public class UserMapper {

    public UserDTO toDto(User user) {
        return new UserDTO(
                user.getUsername()
        );
    }
}
