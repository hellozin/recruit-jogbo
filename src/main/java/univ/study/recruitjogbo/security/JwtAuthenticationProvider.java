package univ.study.recruitjogbo.security;

import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import univ.study.recruitjogbo.error.NotFoundException;
import univ.study.recruitjogbo.member.Member;
import univ.study.recruitjogbo.member.MemberService;
import univ.study.recruitjogbo.member.Role;

import static org.springframework.security.core.authority.AuthorityUtils.createAuthorityList;
import static org.springframework.util.ClassUtils.isAssignable;

@AllArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final JWT jwt;

    private final MemberService memberService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) authentication;
        return processMemberAuthentication(authenticationToken.authenticationRequest());
    }

    private Authentication processMemberAuthentication(AuthenticationRequest request) {
        try {
            Member member = memberService.login(request.getPrincipal(), request.getCredentials());
            JwtAuthenticationToken authenticated = new JwtAuthenticationToken(member.getId(), null, createAuthorityList(Role.MEMBER.value()));
            String apiToken = jwt.newToken(JWT.Claims.of(member.getId(), member.getName(), member.getEmail(), new String[]{Role.MEMBER.value()}));

            return authenticated;
        } catch (NotFoundException e) {
            throw new UsernameNotFoundException(e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new BadCredentialsException(e.getMessage());
        } catch (DataAccessException e) {
            throw new AuthenticationServiceException(e.getMessage(), e);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return isAssignable(JwtAuthenticationToken.class, authentication);
    }
}
