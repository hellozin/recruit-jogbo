package univ.study.recruitjogbo.configure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import univ.study.recruitjogbo.member.MemberService;
import univ.study.recruitjogbo.security.JWT;
import univ.study.recruitjogbo.security.JwtAuthenticationProvider;
import univ.study.recruitjogbo.security.JwtAuthenticationTokenFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfigure extends WebSecurityConfigurerAdapter {

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

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                    .disable()
                .headers()
                    .disable()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                .authorizeRequests()
                    .antMatchers("/api/member").permitAll()
                    .antMatchers("/api/auth").permitAll()
                    .antMatchers("/api/confirm/email").permitAll()
                    .antMatchers("/api/**").hasRole("UNCONFIRMED")/*.hasRole("MEMBER") 테스트를 위해 UNCONFIRMED 허용*/
                    .anyRequest().authenticated()
                    .and()
                .formLogin()
                    .disable();
        http
                .addFilterBefore(jwtAuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/h2/**");
    }
}
