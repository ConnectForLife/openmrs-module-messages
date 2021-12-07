package org.openmrs.module.messages.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.net.HttpURLConnection;
import org.openmrs.Concept;
import org.openmrs.ConceptName;
import org.openmrs.Patient;
import org.openmrs.api.PatientService;
import org.openmrs.api.context.Context;
import org.openmrs.module.messages.api.dto.ActorDTO;
import org.openmrs.module.messages.api.dto.DefaultPatientTemplateStateDTO;
import org.openmrs.module.messages.api.dto.MessageDetailsDTO;
import org.openmrs.module.messages.api.dto.PatientTemplateDTO;
import org.openmrs.module.messages.api.exception.ValidationException;
import org.openmrs.module.messages.api.mappers.PatientTemplateMapper;
import org.openmrs.module.messages.api.model.PatientTemplate;
import org.openmrs.module.messages.api.service.DefaultPatientTemplateService;
import org.openmrs.module.messages.api.service.PatientTemplateService;
import org.openmrs.module.messages.domain.criteria.PatientTemplateCriteria;
import org.openmrs.module.messages.web.model.HealthTipCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * Exposes the endpoint related to managing data for default patient templates
 */
@Api(
    value = "Default patient template Details",
    tags = {"REST API for Default patient template Details"}
)
@Controller
@RequestMapping(value = "/messages/defaults")
public class DefaultPatientTemplateController extends BaseRestController {

    @Autowired
    @Qualifier("patientService")
    private PatientService patientService;

    @Autowired
    @Qualifier("messages.patientTemplateService")
    private PatientTemplateService patientTemplateService;

    @Autowired
    @Qualifier("messages.defaultPatientTemplateService")
    private DefaultPatientTemplateService defaultPatientTemplateService;

    @Autowired
    @Qualifier("messages.patientTemplateMapper")
    private PatientTemplateMapper mapper;

    /**
     * Checks whether patient has any patient templates and fetches their details
     *
     * @param id id of patient
     * @return DTO object containing information about available patient templates and their detailed data
     */
    @ApiOperation(
        value = "Fetch patient template details",
        notes = "Fetch patient template details",
        response = DefaultPatientTemplateStateDTO.class
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                code = HttpURLConnection.HTTP_OK,
                message = "Default patient template details fetched"
            ),
            @ApiResponse(
                code = HttpURLConnection.HTTP_INTERNAL_ERROR,
                message = "Default patient template details not fetched"
            ),
            @ApiResponse(
                code = HttpURLConnection.HTTP_NOT_FOUND,
                message = "Person not found"
            )
        }
    )
    @RequestMapping(value = "{patientId}/check", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public DefaultPatientTemplateStateDTO checkDefaultValuesUsed(
        @ApiParam(name = "patientId", value = "patientId", required = true)
        @PathVariable("patientId") Integer id) {
        Patient patient = getPatientById(id);
        List<PatientTemplate> existing = patientTemplateService
            .findAllByCriteria(new PatientTemplateCriteria(patient));

        List<PatientTemplate> lacking =
            defaultPatientTemplateService.findLackingPatientTemplates(patient, existing);

        MessageDetailsDTO details = defaultPatientTemplateService
            .getDetailsForRealAndDefault(patient, lacking);

        return new DefaultPatientTemplateStateDTO(mapper.toDtos(lacking), details, existing.size() > 0);
    }

    /**
     * Saves the default patient templates
     *
     * @param id if of patient
     * @return list of DTO objects containing default patient templates
     */
    @ApiOperation(
        value = "Saves default patient template",
        notes = "Saves default patient template",
        response = PatientTemplateDTO.class
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                code = HttpURLConnection.HTTP_OK,
                message = "Default patient template saved"
            ),
            @ApiResponse(
                code = HttpURLConnection.HTTP_INTERNAL_ERROR,
                message = "Default patient template not saved"
            ),
            @ApiResponse(
                code = HttpURLConnection.HTTP_BAD_REQUEST,
                message = "Person not found"
            )
        }
    )
    @RequestMapping(value = "{patientId}/generate-and-save", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<PatientTemplateDTO> generateDefaultPatientTemplates(
        @ApiParam(name = "patientId", value = "patientId", required = true)
        @PathVariable("patientId") Integer id) {
        return mapper.toDtos(
            defaultPatientTemplateService.generateDefaultPatientTemplates(getPatientById(id))
        );
    }

    /**
     * Gets the list of health Tip Categories in the current user language.
     *
     * @return the List of Health Tip categories, never null
     */
    @ApiOperation(
        value = "Get list of health tip categories",
        notes = "Get list of health tip categories",
        response = ActorDTO.class
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                code = HttpURLConnection.HTTP_OK,
                message = "Health tip categories fetched"
            ),
            @ApiResponse(
                code = HttpURLConnection.HTTP_INTERNAL_ERROR,
                message = "Health tip categories not fetched"
            )
        }
    )
    @RequestMapping(value = "healthTipCategories", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<HealthTipCategory> getHealthTipCategoriesForUser() {
        final List<Concept> healthTipCategoryConcepts = defaultPatientTemplateService.getHealthTipCategoryConcepts();
        final Locale currentUserLocale = Context.getLocale();

        final List<HealthTipCategory> categories = new LinkedList<>();

        for (final Concept healthTipCategoryConcept : healthTipCategoryConcepts) {
            final ConceptName shortName = healthTipCategoryConcept.getShortNameInLocale(currentUserLocale);
            final String healthTipCategoryLabel =
                    shortName != null ? shortName.getName() : healthTipCategoryConcept.getDisplayString();

            categories.add(new HealthTipCategory(healthTipCategoryLabel, healthTipCategoryConcept.getDisplayString()));
        }

        return categories;
    }

    private Patient getPatientById(Integer id) {
        if (id == null) {
            throw new ValidationException("Provided id cannot be null");
        }

        Patient patient = patientService.getPatient(id);
        if (patient == null) {
            throw new ValidationException(String.format("Patient with id %s doesn't exist", id));
        }
        return patient;
    }
}
