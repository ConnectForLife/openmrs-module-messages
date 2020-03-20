package org.openmrs.module.messages.api.dto;

import org.apache.commons.lang.NotImplementedException;

public interface DTO {
    /**
     * Necessary information to support CRUD operations.
     * It is not required to implement this functionality when DTO will be used only
     * in one-way communication - from the backend to the frontend.
     *
     * @return id of existing entity or null if it is new
     * @throws NotImplementedException when the DTO is used in CRUD operations
     */
    Integer getId();
}
