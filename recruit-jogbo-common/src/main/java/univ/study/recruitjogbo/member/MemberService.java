package univ.study.recruitjogbo.member;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import univ.study.recruitjogbo.error.NotFoundException;
import univ.study.recruitjogbo.member.confirm.ConfirmationToken;
import univ.study.recruitjogbo.member.confirm.ConfirmationTokenRepository;
import univ.study.recruitjogbo.message.EmailConfirmRequestMessage;

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

    private final RabbitTemplate rabbitTemplate;

    @Transactional
    public Member login(@NotBlank String username, @NotBlank String password) {
        Member member = findByUsername(username)
                .orElseThrow(() -> new NotFoundException(Member.class, username));

        if (!member.checkPassword(passwordEncoder, password)) {
            throw new IllegalArgumentException("Incorrect password.");
        }
        log.info("로그인 하였습니다. 로그인 한 멤버: [{}]", member);
        return member;
    }

    @Transactional
    public Member join(@NotBlank String username,
                       @NotBlank String password,
                       @Email String email) {
        Member member = save(new Member.MemberBuilder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .email(email)
                .build()
        );
        log.info("새로운 멤버가 추가되었습니다. 추가된 멤버: [{}]", member);
        return member;
    }

    @Transactional
    public Member joinWithEmailConfirm(@NotBlank String username,
                                       @NotBlank String password,
                                       @Email String email) {
        Member member = join(username, password, email);
        sendConfirmEmail(member.getEmail());
        return member;
    }

    @Transactional
    public void sendConfirmEmail(@Email String email) {
        ConfirmationToken token = new ConfirmationToken(email);
        confirmationTokenRepository.save(token);

        rabbitTemplate.convertAndSend(
                "email.confirm.exchange",
                "email.confirm.request",
                new EmailConfirmRequestMessage(email, token.getConfirmationToken())
        );
        log.info("인증 메일을 발송했습니다. 보낸 메일 주소: [{}]", email);
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
        if(isConfirmed) {
            confirmationTokenRepository.delete(confirmationToken);
        }
        log.info("이메일 인증 요청이 처리되었습니다. 처리결과: [{}]", isConfirmed);
        return isConfirmed;
    }

    @Transactional(readOnly = true)
    public Optional<Member> findById(@NotNull Long id) {
        return memberRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Member> findByUsername(@NotNull String username) {
        return memberRepository.findByUsername(username);
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
