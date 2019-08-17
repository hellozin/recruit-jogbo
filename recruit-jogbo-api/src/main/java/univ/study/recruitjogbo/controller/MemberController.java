package univ.study.recruitjogbo.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import univ.study.recruitjogbo.member.Member;
import univ.study.recruitjogbo.member.MemberService;
import univ.study.recruitjogbo.request.JoinRequest;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/member")
    public Member join(@RequestBody @Valid JoinRequest joinRequest) {
        return memberService.join(
                joinRequest.getMemberId(),
                joinRequest.getPassword(),
                joinRequest.getName(),
                joinRequest.getEmail()
        );
    }

    @GetMapping("/member/list")
    public List<Member> members() {
        return memberService.findAll();
    }

    @RequestMapping(value = "/confirm/email", method = { RequestMethod.GET, RequestMethod.POST })
    public String confirmEmail(@RequestParam String token) {
        boolean isConfirmed = memberService.confirmEmailByToken(token);
        return isConfirmed
                ? "이메일 인증이 성공적으로 처리되었습니다."
                : "이메일 인증에 실패하였습니다.";
    }

}
