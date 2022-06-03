/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.service.impl;

import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.messages.api.dao.TemplateDao;
import org.openmrs.module.messages.api.dto.TemplateDTO;
import org.openmrs.module.messages.api.mappers.TemplateMapper;
import org.openmrs.module.messages.api.model.Template;
import org.openmrs.module.messages.api.service.TemplateService;
import org.openmrs.module.messages.domain.PagingInfo;
import org.openmrs.module.messages.domain.criteria.BaseCriteria;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Implements methods related to creating, reading, updating and deleting template entities
 */
@Transactional
public class TemplateServiceImpl extends BaseOpenmrsService implements TemplateService {

    private TemplateMapper templateMapper;
    private TemplateDao templateDao;

    @Override
    @Transactional(readOnly = true)
    public Template getById(Serializable id) throws APIException {
        return getTemplateDao().getById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Template> getAll(boolean includeRetired) throws APIException {
        return getTemplateDao().getAll(includeRetired);
    }

    @Override
    public Template saveOrUpdate(Template newOrPersisted) throws APIException {
        return getTemplateDao().saveOrUpdate(newOrPersisted);
    }

    @Override
    public Template saveOrUpdateByDto(TemplateDTO dto) {
        Template toSave = getTemplateDao().getById(dto.getId());
        if (toSave == null) {
            toSave = templateMapper.fromDto(dto);
        } else {
            templateMapper.updateFromDto(dto, toSave);
        }
        return getTemplateDao().saveOrUpdate(toSave);
    }

    @Override
    public List<Template> saveOrUpdateByDtos(List<TemplateDTO> templateDtos) {
        ArrayList<Template> savedOrUpdated = new ArrayList<>();
        for (TemplateDTO dto : templateDtos) {
            savedOrUpdated.add(saveOrUpdateByDto(dto));
        }
        return savedOrUpdated;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Template> findAllByCriteria(BaseCriteria criteria) {
        return findAllByCriteria(criteria, null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Template> findAllByCriteria(BaseCriteria criteria, PagingInfo paging) {
        return getTemplateDao().findAllByCriteria(criteria, paging);
    }

    @Override
    @Transactional(readOnly = true)
    public Template findOneByCriteria(BaseCriteria criteria) {
        return getTemplateDao().findOneByCriteria(criteria);
    }

    public void setTemplateMapper(TemplateMapper templateMapper) {
        this.templateMapper = templateMapper;
    }

    private TemplateDao getTemplateDao() {
        if (templateDao == null) {
            templateDao = Context.getRegisteredComponent("messages.TemplateDao", TemplateDao.class);
        }
        return templateDao;
    }
}
