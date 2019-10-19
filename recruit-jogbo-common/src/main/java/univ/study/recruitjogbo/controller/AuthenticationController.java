package univ.study.recruitjogbo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import univ.study.recruitjogbo.api.request.AuthenticationRequest;
import univ.study.recruitjogbo.api.response.ApiResponse;
import univ.study.recruitjogbo.api.response.AuthenticationResult;
import univ.study.recruitjogbo.security.JwtAuthenticationToken;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;

    @PostMapping("/auth")
    public ApiResponse authenticateMember(@RequestBody AuthenticationRequest request) {
        AuthenticationResult authenticationResult = (AuthenticationResult) authenticationManager.authenticate(
                new JwtAuthenticationToken(request.getPrincipal(), request.getCredentials())).getDetails();
        return ApiResponse.OK(authenticationResult);
    }

}
