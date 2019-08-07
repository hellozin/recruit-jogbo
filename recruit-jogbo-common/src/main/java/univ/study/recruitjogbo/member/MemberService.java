package univ.study.recruitjogbo.member;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import univ.study.recruitjogbo.error.NotFoundException;
import univ.study.recruitjogbo.member.confirm.ConfirmationToken;
import univ.study.recruitjogbo.member.confirm.ConfirmationTokenRepository;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Validated
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;

    private final ConfirmationTokenRepository confirmationTokenRepository;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Member login(@NotBlank String memberId, @NotBlank String password) {
        Member member = findByMemberId(memberId)
                .orElseThrow(() -> new NotFoundException(Member.class, memberId));

        if (!member.checkPassword(passwordEncoder, password)) {
            throw new IllegalArgumentException("Incorrect password.");
        }
        log.info("로그인 하였습니다. 로그인 한 멤버: [{}]", member);
        return member;
    }

    @Transactional
    public Member join(@NotBlank String memberId,
                       @NotBlank String password,
                       @NotBlank String name,
                       @Email String email) {
        Member member = save(new Member.MemberBuilder()
                .memberId(memberId)
                .password(passwordEncoder.encode(password))
                .name(name)
                .email(email)
                .build()
        );
        log.info("새로운 멤버가 추가되었습니다. 추가된 멤버: [{}]", member);
        return member;
    }

    @Transactional
    public Member joinWithEmailConfirm(@NotBlank String memberId,
                                       @NotBlank String password,
                                       @NotBlank String name,
                                       @Email String email) {
        Member member = join(memberId, password, name, email);
        sendConfirmEmail(member.getEmail());
        return member;
    }

    @Transactional
    public void sendConfirmEmail(@Email String email) {
        ConfirmationToken token = new ConfirmationToken(email);
        confirmationTokenRepository.save(token);

        String subject = "[Recruit Jogbo] 이메일 인증 요청입니다.";
        String text = "이메일을 인증하기 위해 다음 링크를 클릭해 주세요.\n 링크: " +
                "http://localhost:8080/api/confirm/email?token="+token.getConfirmationToken();

//        mailService.send(email, subject, text);
        log.info("인증 메일을 발송했습니다. 발송된 주소: [{}]", email);
    }

    @Transactional
    public boolean confirmEmailByToken(@NotBlank String token) {
        ConfirmationToken confirmationToken = confirmationTokenRepository.findByConfirmationToken(token)
                .orElseThrow(() -> new NotFoundException(ConfirmationToken.class, token));

        String confirmedEmail = confirmationToken.getUserEmail();
        boolean isConfirmed = findByEmail(confirmedEmail)
                .map(member -> member.setEmailConfirmed(true))
                .map(Member::isEmailConfirmed)
                .orElseThrow(() -> new NotFoundException(Member.class, confirmedEmail));
        log.info("이메일 인증 요청이 처리되었습니다. 처리결과: [{}]", isConfirmed);
        return isConfirmed;
    }

    @Transactional(readOnly = true)
    public Optional<Member> findById(@NotNull Long id) {
        return memberRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Member> findByMemberId(@NotNull String memberId) {
        return memberRepository.findByMemberId(memberId);
    }

    @Transactional(readOnly = true)
    public Optional<Member> findByEmail(@Email String email) {
        return memberRepository.findByEmail(email);
    }

    @Transactional(readOnly = true)
    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    public Member save(Member member) {
        return memberRepository.save(member);
    }

}
