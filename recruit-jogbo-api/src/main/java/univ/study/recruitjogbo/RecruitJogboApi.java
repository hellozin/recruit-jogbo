package univ.study.recruitjogbo;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class RecruitJogboApi {

    private static final String PROPERTIES =
            "spring.config.location="
            +"classpath:/application.yml"
            +",classpath:/secret-jwt.yml"
            +",file:./secret-rabbitmq.yml"
            +",file:./secret-datasource.yml";

    public static void main(String[] args) {
        new SpringApplicationBuilder(RecruitJogboApi.class)
                .properties(PROPERTIES)
                .run(args);
    }

}
