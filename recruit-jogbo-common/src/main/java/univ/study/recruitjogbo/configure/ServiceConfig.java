package univ.study.recruitjogbo.configure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;
import univ.study.recruitjogbo.review.recruitType.RecruitTypes;
import univ.study.recruitjogbo.security.JWT;
import univ.study.recruitjogbo.util.EnumMapper;
import univ.study.recruitjogbo.util.MessageUtils;

@Configuration
public class ServiceConfig {

    @Value("${jwt.token.issuer}")
    String issuer;

    @Value("${jwt.token.clientSecret}")
    String clientSecret;

    @Value("${jwt.token.expirySeconds}")
    int expirySeconds;

    @Bean
    public JWT jwt() {
        return new JWT(issuer, clientSecret, expirySeconds);
    }

    @Bean
    public MessageSourceAccessor messageSourceAccessor(MessageSource messageSource) {
        return new MessageSourceAccessor(messageSource);
    }

    @Bean
    public MessageUtils messageUtils() {
        return MessageUtils.getInstance();
    }

    @Bean
    public EnumMapper enumMapper() {
        EnumMapper enumMapper = new EnumMapper();
        enumMapper.put("recruitTypes", RecruitTypes.class);
        return enumMapper;
    }

}
