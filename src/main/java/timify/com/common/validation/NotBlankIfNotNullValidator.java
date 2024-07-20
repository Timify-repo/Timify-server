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
        // 값이 null이면 유효한 값으로 처리
        if (value == null) {
            return true;
        }
        // 값이 null이 아니면, 공백이 아닌 값을 가져야 유효한 값으로 처리
        return !value.trim().isEmpty();
    }
}
