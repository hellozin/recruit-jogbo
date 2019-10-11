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
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import univ.study.recruitjogbo.member.MemberService;
import univ.study.recruitjogbo.security.*;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.math.NumberUtils.toLong;

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
        Pattern pattern = Pattern.compile("^/api/post/(\\d+)$");
        RequestMatcher requestMatcher = new RegexRequestMatcher(pattern.pattern(), null);
        return new PostEditableVoter(requestMatcher, (String url) -> {
            Matcher matcher = pattern.matcher(url);
            return matcher.find() ? toLong(matcher.group(1), -1) : -1;
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
                    .csrfTokenRepository(new CookieCsrfTokenRepository())
                    .and()
                .exceptionHandling()
                    .accessDeniedHandler(accessDeniedHandlerImpl)
                    .authenticationEntryPoint(entryPointUnauthorizedHandler)
                    .and()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                .authorizeRequests()
                    .antMatchers("/api/auth").permitAll()
                    .antMatchers("/api/member").permitAll()
                    .antMatchers("/member/confirm/request").hasRole("UNCONFIRMED")
                    .antMatchers("/post/**").hasRole("UNCONFIRMED")
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
                .antMatchers("/member/confirm"); // Email authentication URL
    }

}
