package univ.study.recruitjogbo.member;

import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import univ.study.recruitjogbo.request.JoinRequest;
import univ.study.recruitjogbo.security.JwtAuthentication;

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
    public List<Member> members(@AuthenticationPrincipal JwtAuthentication authentication) {
        return memberService.findAll();
    }

}
