package univ.study.recruitjogbo;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class RecruitJogboApi {

    private static final String PROPERTIES =
            "spring.config.location="
            +"classpath:/application.yml"
            +",classpath:/secret.yml";

    public static void main(String[] args) {
//        SpringApplication.run(RecruitJogbo.class, args);
        new SpringApplicationBuilder(RecruitJogboApi.class)
                .properties(PROPERTIES)
                .run(args);
    }

}