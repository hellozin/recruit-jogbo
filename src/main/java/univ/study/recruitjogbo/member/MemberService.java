package univ.study.recruitjogbo.member;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import univ.study.recruitjogbo.error.NotFoundException;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
@Validated
public class MemberService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Member login(@NotBlank String memberId, @NotBlank String password) {
        Member member = findByMemberId(memberId)
                .orElseThrow(() -> new NotFoundException(Member.class, memberId));

        if (!member.checkPassword(passwordEncoder, password)) {
            throw new IllegalArgumentException("Incorrect password.");
        }
        return member;
    }

    @Transactional
    public Member join(@NotBlank String memberId,
                       @NotBlank String password,
                       @NotBlank String name,
                       @Email String email) {
        return save(new Member.MemberBuilder()
                .memberId(memberId)
                .password(passwordEncoder.encode(password))
                .name(name)
                .email(email)
                .build()
        );
    }

    @Transactional(readOnly = true)
    public Optional<Member> findByMemberId(@NotBlank String memberId) {
        return memberRepository.findByMemberId(memberId);
    }

    @Transactional(readOnly = true)
    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    public Member save(Member member) {
        return memberRepository.save(member);
    }

}
