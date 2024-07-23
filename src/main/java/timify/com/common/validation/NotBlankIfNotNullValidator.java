package timify.com.common.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NotBlankIfNotNullValidator implements ConstraintValidator<NotBlankIfNotNull, String> {

    @Override
    public void initialize(NotBlankIfNotNull constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return !value.trim().isEmpty();
    }
}
