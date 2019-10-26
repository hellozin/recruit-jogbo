package univ.study.recruitjogbo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import univ.study.recruitjogbo.api.request.*;
import univ.study.recruitjogbo.api.response.ApiError;
import univ.study.recruitjogbo.api.response.ApiResponse;
import univ.study.recruitjogbo.error.NotFoundException;
import univ.study.recruitjogbo.member.Member;
import univ.study.recruitjogbo.member.MemberService;
import univ.study.recruitjogbo.security.JwtAuthentication;

import javax.validation.Valid;

import static org.apache.commons.lang3.StringUtils.isBlank;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/member")
    public ApiResponse join(@RequestBody @Valid JoinRequest joinRequest) {
        String confirmUrl = joinRequest.getConfirmUrl();
        return isBlank(confirmUrl)
            ? ApiResponse.OK(memberService.join(joinRequest))
            : ApiResponse.OK(memberService.joinWithEmailConfirm(joinRequest, confirmUrl));
    }

    @GetMapping("/member/me")
    public ApiResponse getMember(@AuthenticationPrincipal JwtAuthentication jwtAuthentication) {
        Member member = memberService.findById(jwtAuthentication.id)
                .orElseThrow(() -> new NotFoundException(Member.class, jwtAuthentication.id.toString()));
        return ApiResponse.OK(member);
    }

    @PatchMapping("/member/me")
    public ApiResponse updateMember(@AuthenticationPrincipal JwtAuthentication jwtAuthentication,
                                    @RequestBody @Valid MemberUpdateRequest memberUpdateRequest) {
        return ApiResponse.OK(memberService.update(jwtAuthentication.id, memberUpdateRequest));
    }

    @PatchMapping("/member/me/password")
    public ApiResponse updateMemberPassword(@AuthenticationPrincipal JwtAuthentication jwtAuthentication,
                                            @RequestBody @Valid MemberUpdatePasswordRequest memberUpdatePasswordRequest) {
        return ApiResponse.OK(memberService.updatePassword(
                jwtAuthentication.id,
                memberUpdatePasswordRequest.getOriginPassword(),
                memberUpdatePasswordRequest.getNewPassword()));
    }

    @DeleteMapping("/member/me")
    public ApiResponse deleteMember(@AuthenticationPrincipal JwtAuthentication jwtAuthentication) {
        memberService.delete(jwtAuthentication.id);
        return ApiResponse.OK(jwtAuthentication.id);
    }

    @PostMapping("/member/confirm")
    public ApiResponse confirmEmail(@RequestBody ConfirmEmailRequest confirmEmailRequest) {
        return memberService.confirmEmailByToken(confirmEmailRequest.getToken())
                ? ApiResponse.OK("이메일 인증이 완료되었습니다.")
                : ApiResponse.ERROR(new ApiError("이메일 인증에 실패했습니다.", HttpStatus.BAD_REQUEST));
    }

    @PostMapping("/member/confirm/send")
    public ApiResponse resendConfirmEmail(@AuthenticationPrincipal JwtAuthentication jwtAuthentication,
                                          ConfirmMailSendingRequest confirmMailSendingRequest) {
        Member member = memberService.findById(jwtAuthentication.id)
                .orElseThrow(() -> new NotFoundException(Member.class, jwtAuthentication.id.toString()));
        memberService.sendConfirmEmail(member.getEmail(), confirmMailSendingRequest.getConfirmUrl());
        return ApiResponse.OK("인증 메일이 발송되었습니다.");
    }

}
