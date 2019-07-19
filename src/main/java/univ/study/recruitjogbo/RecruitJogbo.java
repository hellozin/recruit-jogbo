package univ.study.recruitjogbo;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class RecruitJogbo {

    private static final String GOOGLE_YML = "spring.config.location=classpath:/google.yml";
    private static final String MAIL_YML = "spring.config.location=classpath:/mail.yml";

    public static void main(String[] args) {
        new SpringApplicationBuilder(RecruitJogbo.class)
                .properties(GOOGLE_YML, MAIL_YML)
                .run(args);
    }

}
