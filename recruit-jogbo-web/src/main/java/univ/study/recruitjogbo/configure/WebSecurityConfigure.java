package univ.study.recruitjogbo.configure;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.UnanimousBased;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.expression.WebExpressionVoter;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import univ.study.recruitjogbo.security.AccessDeniedHandlerImpl;
import univ.study.recruitjogbo.security.EntryPointUnauthorizedHandler;
import univ.study.recruitjogbo.security.PostEditableVoter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.math.NumberUtils.toLong;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfigure extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;

    private final UserDetailsService userDetailsService;

    private final AccessDeniedHandlerImpl accessDeniedHandlerImpl;

    private final EntryPointUnauthorizedHandler entryPointUnauthorizedHandler;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PostEditableVoter postEditableVoter() {
        Pattern pattern = Pattern.compile("^/post/(\\d+)$");
        RequestMatcher requestMatcher = new RegexRequestMatcher(pattern.pattern(), HttpMethod.PUT.toString());
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
                .exceptionHandling()
                    .accessDeniedHandler(accessDeniedHandlerImpl)
                    .authenticationEntryPoint(entryPointUnauthorizedHandler)
                    .and()
                .authorizeRequests()
                    .antMatchers("/member").permitAll()
                    .antMatchers("/member/confirm/request").hasRole("UNCONFIRMED")
                    .antMatchers("/post/**").hasRole("MEMBER")
                    .anyRequest().authenticated()
                    .accessDecisionManager(accessDecisionManager())
                    .and()
                .formLogin()
                    .loginPage("/login")
                    .defaultSuccessUrl("/post/list?sort=createdAt,desc", true)
                    .permitAll()
                    .and()
                .logout()
                    .logoutSuccessUrl("/login");

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                .antMatchers("/h2/**")
                .antMatchers("/member/confirm") // Email authentication URL
                .antMatchers("/error")
                .antMatchers("/success");
    }

}
