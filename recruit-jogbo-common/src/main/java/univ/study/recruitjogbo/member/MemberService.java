package univ.study.recruitjogbo.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import univ.study.recruitjogbo.api.request.JoinRequest;
import univ.study.recruitjogbo.error.NotFoundException;
import univ.study.recruitjogbo.member.confirm.ConfirmationToken;
import univ.study.recruitjogbo.member.confirm.ConfirmationTokenRepository;
import univ.study.recruitjogbo.message.EmailConfirmRequest;
import univ.study.recruitjogbo.message.RabbitMQ;
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
    public Member join(JoinRequest request) {
        Member member = save(new Member.MemberBuilder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .build()
        );
        log.info("New member joined. [{}]", member.getUsername());

        return member;
    }

    @Transactional
    public Member joinWithEmailConfirm(JoinRequest request, String confirmUrl) {
        Member member = join(request);
        sendConfirmEmail(member.getEmail(), confirmUrl);
        return member;
    }

    @Transactional
    public void sendConfirmEmail(String email, String confirmUrl) {
        String token = confirmationTokenRepository.findByUserEmail(email)
                .map(ConfirmationToken::getConfirmationToken)
                .orElseGet(() -> confirmationTokenRepository
                        .save(new ConfirmationToken(email))
                        .getConfirmationToken());

        final String confirmLink = confirmUrl + "?token=" + token;

        try {
            rabbitTemplate.convertAndSend(
                    RabbitMQ.EXCHANGE,
                    RabbitMQ.CONFIRM_EMAIL_REQUEST,
                    new EmailConfirmRequest(email, confirmLink));
            log.info("Confirmation email send. Send to [{}]", email);
        } catch (AmqpException exception) {
            log.error("Sending email confirm message failed. with {}", email);
        }
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
