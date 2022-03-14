package org.openmrs.module.messages.api.dto;

/**
 * Represents a user DTO
 */
public class UserDTO extends BaseDTO {

    private static final long serialVersionUID = -341881254828879744L;

    private String username;

    public UserDTO(String username) {
        this.username = username;
    }

    public UserDTO() { }

    public String getUsername() {
        return username;
    }
}
