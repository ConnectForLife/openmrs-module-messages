package org.openmrs.module.messages.api.dao;

import org.openmrs.api.db.OpenmrsMetadataDAO;
import org.openmrs.module.messages.api.model.Template;
import org.openmrs.module.messages.domain.PagingInfo;
import org.openmrs.module.messages.domain.criteria.BaseCriteria;

import java.util.List;

/**
 * Data access object for the Template entities
 */
public interface TemplateDao extends OpenmrsMetadataDAO<Template> {

    /**
     * Finds paginated collection of the objects of the specified type
     *
     * @param criteria   object representing searching criteria
     * @param pagingInfo object representing pagination parameters (eg. page size)
     * @return list of the objects of the specified type, implicitly paginated
     */
    List<Template> findAllByCriteria(BaseCriteria criteria, PagingInfo pagingInfo);

    /**
     * Gets count of all Template which fit {@code criteria}.
     *
     * @param criteria object representing searching criteria
     * @return the count of Template entities which fits the {@code criteria}
     */
    long getCountByCriteria(BaseCriteria criteria);
}
