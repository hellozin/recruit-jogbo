package univ.study.recruitjogbo.member;

import lombok.RequiredArgsConstructor;
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
import univ.study.recruitjogbo.validator.UnivEmail;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
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
        log.info("Login with [{}]", member.getUsername());
        return member;
    }

    @Transactional
    public Member join(@NotBlank String username,
                       @NotBlank String password,
                       @UnivEmail String email) {
        Member member = save(new Member.MemberBuilder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .email(email)
                .build()
        );
        log.info("New member joined. [{}]", member.getUsername());
//        rabbitTemplate.convertAndSend(
//                "member",
//                "member.create",
//                new MemberEventMessage(member.getId(), member.getUsername(), member.getEmail()));
        return member;
    }

    @Transactional
    public Member joinWithEmailConfirm(@NotBlank String username,
                                       @NotBlank String password,
                                       @UnivEmail String email,
                                       String confirmUrl) {
        Member member = join(username, password, email);
        sendConfirmEmail(member.getEmail(), confirmUrl);
        return member;
    }

    @Transactional
    public void sendConfirmEmail(@UnivEmail String email, String confirmUrl) {
        String token = confirmationTokenRepository.findByUserEmail(email)
                .map(ConfirmationToken::getConfirmationToken)
                .orElseGet(() -> confirmationTokenRepository
                        .save(new ConfirmationToken(email))
                        .getConfirmationToken());

        final String confirmLink = confirmUrl + "?token=" + token;

        rabbitTemplate.convertAndSend(
                "email.confirm.exchange",
                "email.confirm.request",
                new EmailConfirmRequestMessage(email, confirmLink));

        log.info("Confirmation email send. Send to [{}]", email);
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

        log.info("Email confirmation finished. isConfirmed [{}]", isConfirmed);
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
    public Optional<Member> findByEmail(@UnivEmail String email) {
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
