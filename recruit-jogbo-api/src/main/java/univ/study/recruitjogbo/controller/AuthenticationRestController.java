package univ.study.recruitjogbo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import univ.study.recruitjogbo.error.UnauthorizedException;
import univ.study.recruitjogbo.security.AuthenticationRequest;
import univ.study.recruitjogbo.security.AuthenticationResult;
import univ.study.recruitjogbo.security.JwtAuthenticationToken;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationRestController {

    private final AuthenticationManager authenticationManager;

    @PostMapping
    public AuthenticationResult authenticate(@RequestBody AuthenticationRequest request) {
        try {
            JwtAuthenticationToken authenticationToken =
                    new JwtAuthenticationToken(request.getPrincipal(), request.getCredentials());
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            return (AuthenticationResult) authentication.getDetails();
        } catch (AuthenticationException e) {
            throw new UnauthorizedException(e.getMessage());
        }
    }

}
