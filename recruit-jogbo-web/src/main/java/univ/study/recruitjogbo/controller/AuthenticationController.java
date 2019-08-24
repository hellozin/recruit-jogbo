package univ.study.recruitjogbo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class AuthenticationController {

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

}
