package univ.study.recruitjogbo.configure;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final Environment environment;

//    private static String frontEndOrigin = "http://www.hellozin.net";
    private static String frontEndOrigin = "*";

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new SearchArgumentResolver());
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        if (Arrays.asList(environment.getActiveProfiles()).contains("local")) {
            frontEndOrigin = "*";
        }
        registry.addMapping("/**")
                .allowedOrigins(frontEndOrigin)
                .allowedMethods(
                        HttpMethod.GET.name(),
                        HttpMethod.HEAD.name(),
                        HttpMethod.POST.name(),
                        HttpMethod.PUT.name(),
                        HttpMethod.PATCH.name(),
                        HttpMethod.DELETE.name());
    }

}
