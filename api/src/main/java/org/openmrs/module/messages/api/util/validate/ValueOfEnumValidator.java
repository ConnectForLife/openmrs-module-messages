package org.openmrs.module.messages.api.util.validate;

import java.util.ArrayList;
import java.util.List;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValueOfEnumValidator implements ConstraintValidator<ValueOfEnum, CharSequence> {
    private List<String> acceptedValues;
    
    @Override
    public void initialize(ValueOfEnum annotation) {
        acceptedValues = new ArrayList<>();
        for (Enum enumElement : annotation.enumClass().getEnumConstants()) {
            acceptedValues.add(enumElement.name());
        }
    }
    
    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        return value == null || acceptedValues.contains(value.toString());
    }
}
