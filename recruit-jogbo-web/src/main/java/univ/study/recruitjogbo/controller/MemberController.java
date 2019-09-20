package univ.study.recruitjogbo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import univ.study.recruitjogbo.error.NotFoundException;
import univ.study.recruitjogbo.member.Member;
import univ.study.recruitjogbo.member.MemberService;
import univ.study.recruitjogbo.request.JoinRequest;
import univ.study.recruitjogbo.security.AuthMember;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/member")
    public String signUpForm(JoinRequest joinRequest) {
        return "member/signUp";
    }

    @PostMapping("/member")
    public String signUp(@Valid JoinRequest joinRequest, BindingResult bindingResult, HttpServletRequest request) {

        if (memberService.findByUsername(joinRequest.getUsername()).isPresent()) {
            bindingResult.rejectValue("username", "duplicate.username", "중복된 아이디입니다.");
        }

        if (memberService.findByEmail(joinRequest.getEmail()).isPresent()) {
            bindingResult.rejectValue("email", "duplicate.email", "이미 사용중인 Email 입니다.");
        }

        if (bindingResult.hasErrors()) {
            return "member/signUp";
        }

        String confirmUrl = String.format("%s://%s:%d/member/confirm", request.getScheme(), request.getServerName(), request.getServerPort());
        memberService.joinWithEmailConfirm(joinRequest, confirmUrl);

        return "redirect:/login";
    }

    @PostMapping("/member/confirm/request")
    public String requestEmailConfirm(@AuthenticationPrincipal AuthMember member, HttpServletRequest request) {
        String email = memberService.findById(member.getId())
                .map(Member::getEmail)
                .orElseThrow(() -> new NotFoundException(Member.class, member.getId().toString()));

        String confirmUrl = String.format("%s://%s:%d/member/confirm", request.getScheme(), request.getServerName(), request.getServerPort());
        memberService.sendConfirmEmail(email, confirmUrl);

        SecurityContextHolder.clearContext();
        return "redirect:/success";
    }

    @RequestMapping("/member/confirm")
    public String emailConfirm(@RequestParam String token) {
        boolean isConfirmMailSendSuccess = memberService.confirmEmailByToken(token);
        if (isConfirmMailSendSuccess) {
            return "redirect:/success";
        } else {
            return "redirect:/error";
        }
    }

}
