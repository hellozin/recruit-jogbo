package univ.study.recruitjogbo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import univ.study.recruitjogbo.error.NotFoundException;
import univ.study.recruitjogbo.member.Member;
import univ.study.recruitjogbo.member.MemberService;
import univ.study.recruitjogbo.request.JoinRequest;
import univ.study.recruitjogbo.security.AuthMember;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/member")
    public String signUpForm(JoinRequest request) {
        return "member/signUp";
    }

    @PostMapping("/member")
    public String signUp(@Valid JoinRequest request, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "member/signUp";
        }

        memberService.join(
                request.getUsername(),
                request.getPassword(),
                request.getEmail()
        );
        return "redirect:/login";
    }

    @PostMapping("/member/confirm")
    public String emailConfirm(@AuthenticationPrincipal AuthMember member) {
        String email = memberService.findById(member.getId())
                .map(Member::getEmail)
                .orElseThrow(() -> new NotFoundException(Member.class, member.getId().toString()));
        memberService.sendConfirmEmail(email);
        SecurityContextHolder.clearContext();
        return "redirect:/success";
    }

}
