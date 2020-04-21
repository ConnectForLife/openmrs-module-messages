package org.openmrs.module.messages.api.service.impl;

import org.openmrs.api.APIException;
import org.openmrs.module.messages.api.dto.TemplateDTO;
import org.openmrs.module.messages.api.mappers.TemplateMapper;
import org.openmrs.module.messages.api.model.Template;
import org.openmrs.module.messages.api.service.TemplateService;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements methods related to creating, reading, updating and deleting template entities
 */
public class TemplateServiceImpl extends BaseOpenmrsDataService<Template> implements TemplateService {

    private TemplateMapper templateMapper;

    @Override
    @Transactional
    public Template saveOrUpdateTemplate(Template newOrPersisted) throws APIException {
        return super.saveOrUpdate(newOrPersisted);
    }

    @Override
    @Transactional
    public Template saveOrUpdateByDto(TemplateDTO dto) {
        Template toSave = getById(dto.getId());
        if (toSave == null) {
            toSave = templateMapper.fromDto(dto);
        } else {
            templateMapper.updateFromDto(dto, toSave);
        }
        return saveOrUpdate(toSave);
    }

    @Override
    @Transactional
    public List<Template> saveOrUpdateByDtos(List<TemplateDTO> templateDtos) {
        ArrayList<Template> savedOrUpdated = new ArrayList<>();
        for (TemplateDTO dto : templateDtos) {
            savedOrUpdated.add(saveOrUpdateByDto(dto));
        }
        return savedOrUpdated;
    }

    public void setTemplateMapper(TemplateMapper templateMapper) {
        this.templateMapper = templateMapper;
    }
}
