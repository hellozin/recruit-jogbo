package univ.study.recruitjogbo;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class MailSenderServer {

    private static final String PROPERTIES =
            "spring.config.location="
            +"classpath:/application.yml"
            +",classpath:/mail.yml";

    public static void main(String[] args) {
        new SpringApplicationBuilder(MailSenderServer.class)
                .properties(PROPERTIES)
                .run(args);
    }

}
