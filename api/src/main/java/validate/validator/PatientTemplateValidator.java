package validate.validator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.messages.api.exception.ValidationException;
import org.openmrs.module.messages.api.model.ChannelType;
import org.openmrs.module.messages.api.model.PatientTemplate;
import org.openmrs.module.messages.api.model.Template;
import org.openmrs.module.messages.api.model.TemplateField;
import org.openmrs.module.messages.api.model.TemplateFieldValue;
import org.openmrs.module.messages.api.service.TemplateFieldService;
import org.openmrs.module.messages.api.service.TemplateService;
import org.springframework.util.StringUtils;
import validate.annotation.ValidPatientTemplate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

import static org.openmrs.module.messages.ValidationMessagesConstants.PATIENT_TEMPLATE_REQUIRED_FIELD_IS_EMPTY;
import static org.openmrs.module.messages.ValidationMessagesConstants.TEMPLATE_WITH_ID_NOT_FOUND;
import static org.openmrs.module.messages.api.model.TemplateFieldType.SERVICE_TYPE;

public class PatientTemplateValidator implements ConstraintValidator<ValidPatientTemplate, PatientTemplate> {

    private static final String TEMPLATE_FIELD_VALUES_PATH = "patientTemplate.templateFieldValues";
    private static final String TEMPLATE_ID_PATH = "patientTemplate.templateId";
    private final Log logger = LogFactory.getLog(getClass());

    @Override
    public boolean isValid(PatientTemplate patientTemplate, ConstraintValidatorContext ctx) {
        ctx.disableDefaultConstraintViolation();

        return validateTemplateFields(patientTemplate, ctx);
    }

    private boolean validateTemplateFields(PatientTemplate patientTemplate, ConstraintValidatorContext ctx) {
        List<TemplateFieldValue> emptyButRequired = new ArrayList<>();
        for (TemplateFieldValue templateFieldValue : patientTemplate.getTemplateFieldValues()) {
            validateFieldNotEmpty(emptyButRequired, templateFieldValue);
            Template template = getTemplateService().getById(patientTemplate.getTemplate().getId());
            if (template == null) {
                addErrorToContext(ctx, TEMPLATE_ID_PATH,
                        String.format(TEMPLATE_WITH_ID_NOT_FOUND, patientTemplate.getTemplate().getId()));
                return false;
            }
            validateTemplateFieldValue(templateFieldValue, template.getTemplateFields());
        }

        for (TemplateFieldValue violation : emptyButRequired) {
            addErrorToContext(ctx, TEMPLATE_FIELD_VALUES_PATH,
                    String.format(PATIENT_TEMPLATE_REQUIRED_FIELD_IS_EMPTY, violation.getTemplateField().getId(),
                            getTemplateFieldService().getById(violation.getTemplateField().getId()).getName()));
        }

        return emptyButRequired.size() == 0;
    }

    private void validateFieldNotEmpty(List<TemplateFieldValue> emptyButRequired, TemplateFieldValue templateFieldValue) {
        if (getTemplateFieldService().getById(templateFieldValue.getTemplateField().getId()).getMandatory()
                && StringUtils.isEmpty(templateFieldValue.getValue())) {
            emptyButRequired.add(templateFieldValue);
        }
    }

    private void validateTemplateFieldValue(TemplateFieldValue tfv,
                                            List<TemplateField> templateFields) {
        for (TemplateField tf : templateFields) {
            if (tf.getId().equals(tfv.getTemplateField().getId()) && SERVICE_TYPE.equals(tf.getTemplateFieldType())) {
                validateServiceType(tfv);
            }
        }
    }

    private void validateServiceType(TemplateFieldValue tfv) {
        boolean isValid;
        try {
            ChannelType.fromName(tfv.getValue());
            isValid = true;

        } catch (Exception ex) {
            logger.error(ex);
            isValid = false;
        }
        if (!isValid) {
            throw new ValidationException(String.format("Invalid service type: %s",
                    tfv.getValue()));
        }
    }

    @Override
    public void initialize(ValidPatientTemplate validPatientTemplate) {

    }

    private void addErrorToContext(ConstraintValidatorContext context, String path,
                                   String message) {
        context.buildConstraintViolationWithTemplate(message)
                .addNode(path)
                .addConstraintViolation();
    }

    private TemplateFieldService getTemplateFieldService() {
        return Context.getRegisteredComponent("messages.templateFieldService", TemplateFieldService.class);
    }

    private TemplateService getTemplateService() {
        return Context.getRegisteredComponent("messages.templateService", TemplateService.class);
    }
}
