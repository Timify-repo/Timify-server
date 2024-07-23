package timify.com.common.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = SizeJsonNullableValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface SizeJsonNullable {

    int min() default 0;

    int max() default Integer.MAX_VALUE;

    String message() default "크기가 {min} 에서 {max} 사이여야 합니다.";


    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
