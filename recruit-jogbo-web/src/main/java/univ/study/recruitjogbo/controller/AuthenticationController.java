package univ.study.recruitjogbo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import univ.study.recruitjogbo.request.AuthenticationRequest;
import univ.study.recruitjogbo.security.JwtAuthenticationToken;

@Controller
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;

    @GetMapping("/login")
    public String loginForm(AuthenticationRequest request) {
        return "login";
    }

    @PostMapping("/login")
    public String authenticate(AuthenticationRequest request) {
        try {
            JwtAuthenticationToken authenticationToken =
                    new JwtAuthenticationToken(request.getPrincipal(), request.getCredentials());
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return "redirect:/post/list";
        } catch (AuthenticationException e) {
            return "redirect:/login";
        }
    }

}
