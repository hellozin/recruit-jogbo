package univ.study.recruitjogbo.security;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import univ.study.recruitjogbo.member.Role;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        AuthMember member = (AuthMember) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String redirectUrl = "/login";
        for (GrantedAuthority grantedAuthority : member.getAuthorities()) {
            if (grantedAuthority.getAuthority().equals(Role.UNCONFIRMED.value())) {
                redirectUrl = "/confirm";
            }
        }
        response.sendRedirect(redirectUrl);
    }
}
