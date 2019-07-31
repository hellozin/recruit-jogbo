package univ.study.recruitjogbo;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class RecruitJogbo {

    private static final String PROPERTIES =
            "spring.config.location="
            +"classpath:/application.yml"
            +",classpath:/secret.yml";

    public static void main(String[] args) {
//        SpringApplication.run(RecruitJogbo.class, args);
        new SpringApplicationBuilder(RecruitJogbo.class)
                .properties(PROPERTIES)
                .run(args);
    }

}
