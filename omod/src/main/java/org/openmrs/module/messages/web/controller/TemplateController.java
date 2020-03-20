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
                                      @RequestBody TemplateDTO templateDto) {
        validateTemplateId(templateId);
        Template existing = templateService.getById(templateId);
        validateTemplate(templateDto, existing, templateId);

        Template updated = templateService.saveOrUpdateByDto(templateDto);
        return templateMapper.toDto(updated);
    }

    @RequestMapping(method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public TemplateWrapper updateTemplates(@RequestBody TemplateWrapper templateWrapper) {
        validateTemplates(templateWrapper.getTemplates());
        return new TemplateWrapper(templateMapper.toDtos(
                templateService.saveOrUpdateByDtos(templateWrapper.getTemplates())));
    }

    private void validateTemplates(List<TemplateDTO> templateDtos) {
        for (TemplateDTO dto : templateDtos) {
            Integer templateId = dto.getId();
            validateTemplateId(templateId);
            Template oldTemplate = templateService.getById(templateId);
            validateTemplate(dto, oldTemplate, templateId);
        }
    }

    private void validateTemplate(TemplateDTO templateDTO, Template oldTemplate, Integer templateId) {
        validationComponent.validate(templateDTO);
        validateOldTemplate(templateId, oldTemplate);
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
