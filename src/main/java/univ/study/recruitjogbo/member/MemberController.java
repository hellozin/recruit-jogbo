package univ.study.recruitjogbo.member;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import univ.study.recruitjogbo.request.JoinRequest;

import javax.validation.Valid;
import java.util.List;

@RestController
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/member")
    public Member join(@RequestBody @Valid JoinRequest joinRequest) {
        return memberService.join(
                joinRequest.getUserId(),
                joinRequest.getPassword(),
                joinRequest.getName(),
                joinRequest.getEmail()
        );
    }

    @GetMapping("/member/list")
    public List<Member> members() {
        return memberService.findAll();
    }

}
