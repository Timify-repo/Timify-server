package timify.com.common.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.openapitools.jackson.nullable.JsonNullable;

public class NotBlankJsonNullableValidator implements
    ConstraintValidator<NotBlankJsonNullable, JsonNullable<String>> {

    @Override
    public void initialize(NotBlankJsonNullable constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(JsonNullable<String> value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        if (!value.isPresent() || value.get() == null) {
            return true;
        }

        String actualValue = value.get();
        return !actualValue.trim().isEmpty();
    }
}
