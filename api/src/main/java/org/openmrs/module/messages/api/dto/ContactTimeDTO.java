package org.openmrs.module.messages.api.dto;

/**
 * Represents a contact time DTO
 */
public class ContactTimeDTO extends BaseDTO {

    private static final long serialVersionUID = 7391388329457186931L;

    private Integer personId;

    private String time;

    public Integer getPersonId() {
        return personId;
    }

    public ContactTimeDTO setPersonId(Integer personId) {
        this.personId = personId;
        return this;
    }

    public String getTime() {
        return time;
    }

    public ContactTimeDTO setTime(String time) {
        this.time = time;
        return this;
    }
}
