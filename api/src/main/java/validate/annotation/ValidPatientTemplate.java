package validate.annotation;

import org.openmrs.module.messages.ValidationMessagesConstants;
import validate.validator.PatientTemplateValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Constraint(validatedBy = {PatientTemplateValidator.class})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPatientTemplate {

    /**
     * Specify the message in case of a validation error
     *
     * @return the message about the error
     */
    String message() default ValidationMessagesConstants.PATIENT_TEMPLATE_INVALID;

    /**
     * Specify validation groups, to which this constraint belongs
     *
     * @return array with group classes
     */
    Class<?>[] groups() default {
    };

    /**
     * Specify custom payload objects
     *
     * @return array with payload classes
     */
    Class<? extends Payload>[] payload() default {
    };
}
