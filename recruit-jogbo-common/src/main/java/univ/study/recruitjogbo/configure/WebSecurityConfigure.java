package univ.study.recruitjogbo.configure;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.UnanimousBased;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.expression.WebExpressionVoter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsUtils;
import univ.study.recruitjogbo.member.MemberService;
import univ.study.recruitjogbo.security.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfigure extends WebSecurityConfigurerAdapter {

    private final AccessDeniedHandlerImpl accessDeniedHandlerImpl;

    private final EntryPointUnauthorizedHandler entryPointUnauthorizedHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public JwtAuthenticationProvider jwtAuthenticationProvider(JWT jwt, MemberService memberService) {
        return new JwtAuthenticationProvider(jwt, memberService);
    }

    @Bean
    public JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter() {
        return new JwtAuthenticationTokenFilter();
    }

    @Autowired
    public void configureAuthentication(AuthenticationManagerBuilder builder, JwtAuthenticationProvider authenticationProvider) {
        builder.authenticationProvider(authenticationProvider);
    }

    @Bean
    public PostEditableVoter postEditableVoter() {
        Pattern pattern = Pattern.compile("^/api/(review|tip)/(\\d+)$");
        RequestMatcher requestMatcher = new RegexRequestMatcher(pattern.pattern(), null);
        return new PostEditableVoter(requestMatcher, (String url) -> {
            Matcher matcher = pattern.matcher(url);
            if (matcher.find()) {
                HashMap<String, String> group = new HashMap<>();
                group.put("domain", matcher.group(1));
                group.put("id", matcher.group(2));
                return group;
            } else {
                return Collections.emptyMap();
            }
        });
    }

    @Bean
    public AccessDecisionManager accessDecisionManager() {
        List<AccessDecisionVoter<?>> decisionVoters = new ArrayList<>();
        decisionVoters.add(new WebExpressionVoter());
        decisionVoters.add(postEditableVoter());
        return new UnanimousBased(decisionVoters);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                    .disable()
                .exceptionHandling()
                    .accessDeniedHandler(accessDeniedHandlerImpl)
                    .authenticationEntryPoint(entryPointUnauthorizedHandler)
                    .and()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                .authorizeRequests()
                    .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                    .antMatchers("/api/auth").permitAll()
                    .antMatchers("/api/member").permitAll()
                    .antMatchers("/api/member/confirm/send").hasRole("UNCONFIRMED")
                    .antMatchers("/api/review/**").hasRole("MEMBER")
                    .antMatchers("/api/tip/**").hasRole("MEMBER")
                    .anyRequest().authenticated()
                    .accessDecisionManager(accessDecisionManager())
                    .and()
                .formLogin()
                    .disable();
        http
                .addFilterBefore(jwtAuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                .antMatchers("/")
                .antMatchers("/static/**")
                .antMatchers("/h2/**")
                .antMatchers("/api/hcheck")
                .antMatchers("/api/member/confirm")
                .antMatchers("/api/recruit-types");
    }

}
