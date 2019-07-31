package univ.study.recruitjogbo.configure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import univ.study.recruitjogbo.security.JWT;

@Configuration
@Slf4j
public class ServiceConfig {

    @Value("${jwt.token.issuer}")
    String issuer;

    @Value("${jwt.token.clientSecret}")
    String clientSecret;

    @Value("${jwt.token.expirySeconds}")
    int expirySeconds;

    @Bean
    public JWT jwt() {
        log.info("Jwt Information issuer : {} clientSecret : {}", issuer, clientSecret);
        return new JWT(issuer, clientSecret, expirySeconds);
    }

}
