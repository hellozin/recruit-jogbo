package univ.study.recruitjogbo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import univ.study.recruitjogbo.api.request.JoinRequest;
import univ.study.recruitjogbo.api.response.ApiResponse;
import univ.study.recruitjogbo.error.NotFoundException;
import univ.study.recruitjogbo.member.Member;
import univ.study.recruitjogbo.member.MemberService;
import univ.study.recruitjogbo.security.JwtAuthentication;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/member")
    public ApiResponse join(@RequestBody JoinRequest joinRequest) {
        Member member = memberService.join(joinRequest);
        return ApiResponse.OK(member);
    }

    @GetMapping("/member")
    public ApiResponse getMember(@AuthenticationPrincipal JwtAuthentication jwtAuthentication) {
        Member member = memberService.findById(jwtAuthentication.id)
                .orElseThrow(() -> new NotFoundException(Member.class, jwtAuthentication.id.toString()));
        return ApiResponse.OK(member);
    }

}
