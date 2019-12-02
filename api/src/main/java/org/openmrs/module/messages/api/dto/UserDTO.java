package org.openmrs.module.messages.api.dto;

public class UserDTO {

    private String username;

    public UserDTO(String username) {
        this.username = username;
    }

    public UserDTO() { }

    public String getUsername() {
        return username;
    }
}
