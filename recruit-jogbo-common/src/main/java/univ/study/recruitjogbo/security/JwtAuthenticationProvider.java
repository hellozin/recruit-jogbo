package univ.study.recruitjogbo.security;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import univ.study.recruitjogbo.api.request.AuthenticationRequest;
import univ.study.recruitjogbo.api.response.AuthenticationResult;
import univ.study.recruitjogbo.error.NotFoundException;
import univ.study.recruitjogbo.member.Member;
import univ.study.recruitjogbo.member.MemberService;
import univ.study.recruitjogbo.member.Role;

import static org.springframework.security.core.authority.AuthorityUtils.createAuthorityList;

@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final JWT jwt;

    private final MemberService memberService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) authentication;
        AuthenticationRequest request = authenticationToken.authenticationRequest();
        try {
            Member member = memberService.login(request.getPrincipal(), request.getCredentials());
            Role role = member.isEmailConfirmed() ? Role.MEMBER : Role.UNCONFIRMED;
            JwtAuthenticationToken authenticated = new JwtAuthenticationToken(member.getId(), null, createAuthorityList(role.value()));
            String apiToken = jwt.newToken(JWT.Claims.of(member.getId(), member.getUsername(), member.getEmail(), new String[]{role.value()}));
            authenticated.setDetails(new AuthenticationResult(apiToken, member));
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
        return authentication.isAssignableFrom(JwtAuthenticationToken.class);
    }
}
