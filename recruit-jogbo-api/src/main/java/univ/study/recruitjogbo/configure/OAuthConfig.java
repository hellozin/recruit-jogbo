//package univ.study.recruitjogbo.configure;
//
//import lombok.AllArgsConstructor;
//import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
//import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//import org.springframework.security.oauth2.client.OAuth2ClientContext;
//import org.springframework.security.oauth2.client.OAuth2RestTemplate;
//import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
//import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
//import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
//import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
//import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
//
//import javax.servlet.Filter;
//
////@Configuration
////@EnableOAuth2Client
//@AllArgsConstructor
//public class OAuthConfig {
//
//    private final OAuth2ClientContext oAuth2ClientContext;
//
////    @Bean
//    public Filter ssoFilter() {
//        OAuth2ClientAuthenticationProcessingFilter oAuth2Filter
//                = new OAuth2ClientAuthenticationProcessingFilter("/login");
//        OAuth2RestTemplate oAuth2RestTemplate = new OAuth2RestTemplate(googleClient(), oAuth2ClientContext);
//        oAuth2Filter.setRestTemplate(oAuth2RestTemplate);
//        oAuth2Filter.setTokenServices(new UserInfoTokenServices(
//                googleResource().getUserInfoUri(), googleClient().getClientId()));
//        oAuth2Filter.setAuthenticationSuccessHandler(successHandler());
//        return oAuth2Filter;
//    }
//
////    @Bean
//    public AuthenticationSuccessHandler successHandler() {
//        return (request, response, authentication) -> System.out.println("인증 성공");
//    }
//
////    @Bean
//    @ConfigurationProperties("google.client")
//    public OAuth2ProtectedResourceDetails googleClient() {
//        return new AuthorizationCodeResourceDetails();
//    }
//
////    @Bean
//    @ConfigurationProperties("google.resource")
//    public ResourceServerProperties googleResource() {
//        return new ResourceServerProperties();
//    }
//
////    @Bean
//    public FilterRegistrationBean oAuth2ClientFilterRegistration(OAuth2ClientContextFilter filter) {
//        FilterRegistrationBean<OAuth2ClientContextFilter> registration = new FilterRegistrationBean<>();
//        registration.setFilter(filter);
//        registration.setOrder(-100);
//        return registration;
//    }
//
//}
