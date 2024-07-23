package timify.com.common.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import org.openapitools.jackson.nullable.JsonNullable;

public class PastJsonNullableValidator implements
    ConstraintValidator<PastJsonNullable, JsonNullable<LocalDate>> {

    @Override
    public void initialize(PastJsonNullable PastJsonNullable) {
    }

    @Override
    public boolean isValid(JsonNullable<LocalDate> value, ConstraintValidatorContext context) {

        if (value == null) {
            return true;
        }
        if (!value.isPresent() || value.get() == null) {
            return true;
        }

        LocalDate actualValue = value.get();
        return actualValue.isBefore(LocalDate.now());

    }
}
