package org.openmrs.module.messages.web.controller;

import org.openmrs.module.messages.api.dto.PageDTO;
import org.openmrs.module.messages.api.dto.TemplateDTO;
import org.openmrs.module.messages.api.exception.ValidationException;
import org.openmrs.module.messages.api.mappers.TemplateMapper;
import org.openmrs.module.messages.api.model.ErrorMessage;
import org.openmrs.module.messages.api.model.Template;
import org.openmrs.module.messages.api.service.TemplateService;
import org.openmrs.module.messages.api.util.validate.ValidationComponent;
import org.openmrs.module.messages.domain.PagingInfo;
import org.openmrs.module.messages.domain.criteria.TemplateCriteria;
import org.openmrs.module.messages.web.model.PageableParams;
import org.openmrs.module.messages.web.model.TemplateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * Templates Controller to manage Templates resource
 */
@Controller
@RequestMapping(value = "/messages/templates")
public class TemplateController extends BaseRestController {

    @Autowired
    @Qualifier("messages.templateService")
    private TemplateService templateService;

    @Autowired
    @Qualifier("messages.templateMapper")
    private TemplateMapper templateMapper;

    @Autowired
    @Qualifier("messages.validationComponent")
    private ValidationComponent validationComponent;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public PageDTO<TemplateDTO> getAll(PageableParams pageableParams) {
        PagingInfo pagingInfo = pageableParams.getPagingInfo();
        TemplateCriteria templateCriteria = new TemplateCriteria();
        List<Template> templates = templateService.findAllByCriteria(templateCriteria, pagingInfo);
        return new PageDTO<>(templateMapper.toDtos(templates), pagingInfo);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public TemplateDTO createTemplate(@RequestBody TemplateDTO templateDTO) {
        validationComponent.validate(templateDTO);
        validateIfNewTemplate(templateDTO);
        Template template = templateMapper.fromDto(templateDTO);
        return templateMapper.toDto(templateService.saveOrUpdateTemplate(template));
    }

    @RequestMapping(value = "/{templateId}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public TemplateDTO updateTemplate(@PathVariable Integer templateId,
                                      @RequestBody TemplateDTO templateDTO) {
        validateTemplateId(templateId);
        Template oldTemplate = templateService.getById(templateId);
        validateTemplate(templateDTO, oldTemplate, templateId);
        Template updatedTemplate = updateTemplate(templateDTO, oldTemplate);
        return templateMapper.toDto(templateService.saveOrUpdateTemplate(updatedTemplate));
    }

    @RequestMapping(method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public TemplateWrapper updateTemplates(@RequestBody TemplateWrapper templateWrapper) {
        List<Template> updatedTemplates = validateTemplates(templateWrapper.getTemplates());
        return new TemplateWrapper(templateMapper.toDtos(templateService.saveOrUpdate(updatedTemplates)));
    }

    private List<Template> validateTemplates(List<TemplateDTO> templateDTOS) {
        List<Template> templates = new ArrayList<>();
        for (TemplateDTO dto : templateDTOS) {
            Integer templateId = dto.getId();
            validateTemplateId(templateId);
            Template oldTemplate = templateService.getById(templateId);
            validateTemplate(dto, oldTemplate, templateId);
            templates.add(updateTemplate(dto, oldTemplate));
        }
        return templates;
    }

    private void validateTemplate(TemplateDTO templateDTO, Template oldTemplate, Integer templateId) {
        validationComponent.validate(templateDTO);
        validateOldTemplate(templateId, oldTemplate);
    }

    private Template updateTemplate(TemplateDTO newTemplate, Template oldTemplate) {
        return templateMapper.update(oldTemplate, templateMapper.fromDto(newTemplate));
    }

    private void validateIfNewTemplate(TemplateDTO templateDTO) {
        if (templateDTO.getId() != null) {
            throw new ValidationException(new ErrorMessage("id",
                    "Template isn't a new object (use PUT tu update)."));
        }
    }

    private void validateTemplateId(Integer templateId) {
        if (templateId == null) {
            throw new ValidationException(new ErrorMessage("templateId", "Missing template id value"));
        }
    }

    private void validateOldTemplate(@PathVariable Integer templateId, Template oldTemplate) {
        if (oldTemplate == null) {
            throw new ValidationException(new ErrorMessage("templateId",
                    String.format("The template with %d id doesn't exist", templateId)));
        }
    }
}
