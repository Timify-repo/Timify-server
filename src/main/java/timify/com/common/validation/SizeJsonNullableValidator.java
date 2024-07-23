package timify.com.common.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.openapitools.jackson.nullable.JsonNullable;

public class SizeJsonNullableValidator implements
    ConstraintValidator<SizeJsonNullable, JsonNullable<String>> {

    private int min;
    private int max;

    @Override
    public void initialize(SizeJsonNullable size) {
        this.min = size.min();
        this.max = size.max();
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
        return actualValue.length() >= min && actualValue.length() <= max;


    }
}
