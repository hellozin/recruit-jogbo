package univ.study.recruitjogbo.configure;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.Filter;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class WebSecurityConfigure extends WebSecurityConfigurerAdapter {

    private final Filter ssoFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/", "/h2-console/**", "/favicon.ico", "/login**").permitAll()
                .anyRequest().authenticated()
                .and().logout().logoutSuccessUrl("/").permitAll()
                .and().headers().frameOptions().sameOrigin()
                .and().csrf().disable()
                .addFilterBefore(ssoFilter, BasicAuthenticationFilter.class);
    }

}
