package org.openmrs.module.messages.api.mappers;

import org.openmrs.BaseOpenmrsData;
import org.openmrs.module.messages.api.dto.DTO;

/**
 * The AbstractOpenMrsDataMapper is a base AbstractMapper for OpenMRS Data entities.
 */
public abstract class AbstractOpenMrsDataMapper<T extends DTO, R extends BaseOpenmrsData> extends AbstractMapper<T, R> {

    @Override
    protected void doSafeDelete(R target) {
        target.setVoided(true);
    }
}
