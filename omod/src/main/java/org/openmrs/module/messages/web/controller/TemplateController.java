package org.openmrs.module.messages.web.controller;

import org.openmrs.module.messages.api.dto.PageDTO;
import org.openmrs.module.messages.api.dto.TemplateDTO;
import org.openmrs.module.messages.api.mappers.TemplateMapper;
import org.openmrs.module.messages.api.model.Template;
import org.openmrs.module.messages.api.service.TemplateService;
import org.openmrs.module.messages.web.model.PageableParams;
import org.openmrs.module.messages.domain.PagingInfo;
import org.openmrs.module.messages.domain.criteria.TemplateCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
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

    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public PageDTO<TemplateDTO> getAll(PageableParams pageableParams) {
        PagingInfo pagingInfo = pageableParams.getPagingInfo();
        TemplateCriteria templateCriteria = new TemplateCriteria();
        List<Template> templates = templateService.findAllByCriteria(templateCriteria, pagingInfo);
        return new PageDTO<>(templateMapper.toDtos(templates), pagingInfo);
    }
}
