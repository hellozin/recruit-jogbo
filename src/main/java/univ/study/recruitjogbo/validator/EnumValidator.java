package univ.study.recruitjogbo.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class EnumValidator implements ConstraintValidator<Enum, String> {

    private Enum annotation;

    @Override
    public void initialize(Enum constraintAnnotation) {
        this.annotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        boolean result = false;
        Object[] enumValues = this.annotation.enumClass().getEnumConstants();
        if (enumValues != null) {
            result = Arrays.stream(enumValues).anyMatch(enumValue -> value.equals(enumValue.toString())
                    || (this.annotation.ignoreCase() && value.equalsIgnoreCase(enumValue.toString())));
        }
        return result;
    }

}
