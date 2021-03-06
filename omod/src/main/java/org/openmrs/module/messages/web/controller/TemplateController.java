package org.openmrs.module.messages.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.net.HttpURLConnection;
import org.openmrs.module.messages.api.dto.PageDTO;
import org.openmrs.module.messages.api.dto.TemplateDTO;
import org.openmrs.module.messages.api.exception.ValidationException;
import org.openmrs.module.messages.api.mappers.TemplateMapper;
import org.openmrs.module.messages.api.model.ErrorMessage;
import org.openmrs.module.messages.api.model.Template;
import org.openmrs.module.messages.api.service.NotificationTemplateService;
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
@Api(
    value = "Manage Template resources",
    tags = {"REST API for managing template resources"}
)
@Controller
@RequestMapping(value = "/messages/templates")
public class TemplateController extends BaseRestController {

    @Autowired
    @Qualifier("messages.templateService")
    private TemplateService templateService;

    @Autowired
    private NotificationTemplateService notificationTemplateService;

    @Autowired
    @Qualifier("messages.templateMapper")
    private TemplateMapper templateMapper;

    @Autowired
    @Qualifier("messages.validationComponent")
    private ValidationComponent validationComponent;

    /**
     * Fetches all available templates
     *
     * @param pageableParams parameters representing expected page shape
     * @return a page containing available templates
     */
    @ApiOperation(
        value = "Fetch all available templates",
        notes = "Fetch all available templates",
        response = TemplateDTO.class
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                code = HttpURLConnection.HTTP_OK,
                message = "Available templates fetched"
            ),
            @ApiResponse(
                code = HttpURLConnection.HTTP_INTERNAL_ERROR,
                message = "Available templates not fetched"
            )
        }
    )
    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public PageDTO<TemplateDTO> getAll(PageableParams pageableParams) {
        PagingInfo pagingInfo = pageableParams.getPagingInfo();
        List<Template> templates = templateService.findAllByCriteria(TemplateCriteria.nonRetired(), pagingInfo);
        return new PageDTO<>(templateMapper.toDtos(templates), pagingInfo);
    }

    /**
     * Gets names of Notification Template Global Properties required for all non-retired Message Templates.
     *
     * @return the list of Strings, never null
     */
    @ApiOperation(
        value = "Get names of notification template global properties",
        notes = "Get names of notification template global properties",
        response = String.class
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                code = HttpURLConnection.HTTP_OK,
                message = "Names of notification templates fetched"
            ),
            @ApiResponse(
                code = HttpURLConnection.HTTP_INTERNAL_ERROR,
                message = "Names of notification templates not fetched"
            )
        }
    )
    @RequestMapping(method = RequestMethod.GET, value = "/globalProperties")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<String> getRequiredNotificationTemplatePropertyNames() {
        return notificationTemplateService.getRequiredNotificationTemplatePropertyNames();
    }

    /**
     * Creates a new template
     *
     * @param templateDTO DTO object containing all necessary data to create template
     * @return DTO object containing data related to new template
     */
    @ApiOperation(
        value = "Create A new Template",
        notes = "Create a new Template",
        response = TemplateDTO.class
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                code = HttpURLConnection.HTTP_CREATED,
                message = "Template created"
            ),
            @ApiResponse(
                code = HttpURLConnection.HTTP_INTERNAL_ERROR,
                message = "Template not created"
            )
        }
    )
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public TemplateDTO createTemplate(@RequestBody TemplateDTO templateDTO) {
        validationComponent.validate(templateDTO);
        validateIfNewTemplate(templateDTO);
        Template template = templateMapper.fromDto(templateDTO);
        return templateMapper.toDto(templateService.saveOrUpdate(template));
    }

    /**
     * Updates existing template
     *
     * @param templateId  id of existing template
     * @param templateDto DTO object containing data needed to update template
     * @return DTO object containing data related to updated template
     */
    @ApiOperation(
        value = "Update template with given Id",
        notes = "Update template with given Id",
        response = TemplateDTO.class
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                code = HttpURLConnection.HTTP_OK,
                message = "Template with Id updated"
            ),
            @ApiResponse(
                code = HttpURLConnection.HTTP_INTERNAL_ERROR,
                message = "Template with Id not updated"
            ),
            @ApiResponse(
                code = HttpURLConnection.HTTP_NOT_FOUND,
                message = "Template not found"
            )
        }
    )
    @RequestMapping(value = "/{templateId}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public TemplateDTO updateTemplate(
        @ApiParam(name = "templateId", value = "templateId", required = true)
        @PathVariable Integer templateId,
        @ApiParam(name = "templateDto", value = "templateDto", required = true)
        @RequestBody TemplateDTO templateDto) {
        validateTemplateId(templateId);
        Template existing = templateService.getById(templateId);
        validateTemplate(templateDto, existing, templateId);

        Template updated = templateService.saveOrUpdateByDto(templateDto);
        return templateMapper.toDto(updated);
    }

    /**
     * Updates existing templates
     *
     * @param templateWrapper template wrapper object stores list of templates
     * @return template wrapper object with updated list of templates
     */
    @ApiOperation(
        value = "Update existing template details",
        notes = "Update existing template details",
        response = TemplateWrapper.class
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                code = HttpURLConnection.HTTP_OK,
                message = "Template details updated"
            ),
            @ApiResponse(
                code = HttpURLConnection.HTTP_INTERNAL_ERROR,
                message = "Template details not updated"
            ),
            @ApiResponse(
                code = HttpURLConnection.HTTP_NOT_FOUND,
                message = "Template not found"
            )
        }
    )
    @RequestMapping(method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public TemplateWrapper updateTemplates(
        @ApiParam(name = "templateWrapper", value = "templateWrapper", required = true)
        @RequestBody TemplateWrapper templateWrapper) {
        validateTemplates(templateWrapper.getTemplates());
        return new TemplateWrapper(
                templateMapper.toDtos(templateService.saveOrUpdateByDtos(templateWrapper.getTemplates())));
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
            throw new ValidationException(new ErrorMessage("id", "Template isn't a new object (use PUT tu update)."));
        }
    }

    private void validateTemplateId(Integer templateId) {
        if (templateId == null) {
            throw new ValidationException(new ErrorMessage("templateId", "Missing template id value"));
        }
    }

    private void validateOldTemplate(@PathVariable Integer templateId, Template oldTemplate) {
        if (oldTemplate == null) {
            throw new ValidationException(
                    new ErrorMessage("templateId", String.format("The template with %d id doesn't exist", templateId)));
        }
    }
}
