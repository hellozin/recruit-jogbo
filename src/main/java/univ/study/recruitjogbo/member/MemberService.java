package univ.study.recruitjogbo.member;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

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
