package univ.study.recruitjogbo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import univ.study.recruitjogbo.member.MemberService;
import univ.study.recruitjogbo.request.JoinRequest;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/member/new")
    public String signUpForm(JoinRequest request) {
        return "signUp";
    }

    @PostMapping("/member/new")
    public String signUp(JoinRequest request) {
        memberService.join(
                request.getMemberId(),
                request.getPassword(),
                request.getName(),
                request.getEmail()
        );
        return "redirect:/login";
    }

}
