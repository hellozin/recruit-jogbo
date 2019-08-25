package univ.study.recruitjogbo.validator;

import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Slf4j
public class UnivEmailValidator implements ConstraintValidator<UnivEmail, String> {

    private static final String[] domains = {
            "yu.ac.kr",
            "ynu.ac.kr"
    };

    @Override
    public void initialize(UnivEmail constraintAnnotation) {
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (isBlank(email)) {
            return false;
        }

        return Arrays.stream(domains)
                .map(domain -> domain.replace(".", "\\."))
                .anyMatch(domain -> email.matches("\\w+@" + domain));
    }

}
