package univ.study.recruitjogbo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import univ.study.recruitjogbo.member.MemberService;

@Controller
@RequiredArgsConstructor
public class AuthenticationController {

    private final MemberService memberService;

    @GetMapping("/login")
    public String loginForm() {
        return "member/login";
    }

    @GetMapping("/confirm")
    public String confirmForm() {
        return "member/confirm";
    }

    @GetMapping("/success")
    public String success() {
        return "success";
    }

}
