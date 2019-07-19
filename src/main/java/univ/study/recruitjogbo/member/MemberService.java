package univ.study.recruitjogbo.member;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Member join(String userId, String password, String name, String email) {
        return save(new Member.MemberBuilder()
                .userId(userId)
                .password(password)
                .name(name)
                .email(email)
                .build()
        );
    }

    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    public Member save(Member member) {
        return memberRepository.save(member);
    }

}
