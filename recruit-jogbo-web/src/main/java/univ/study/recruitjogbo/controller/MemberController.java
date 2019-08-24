package univ.study.recruitjogbo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import univ.study.recruitjogbo.member.MemberService;
import univ.study.recruitjogbo.request.JoinRequest;

import javax.validation.Valid;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/member/new")
    public String signUpForm(JoinRequest request) {
        return "signUp";
    }

    @PostMapping("/member/new")
    public String signUp(@Valid JoinRequest request, BindingResult bindingResult, Map<String, Object> model) {

        if (bindingResult.hasErrors()) {
            return "signUp";
        }

        memberService.join(
                request.getUsername(),
                request.getPassword(),
                request.getEmail()
        );
        return "redirect:/login";
    }

}
