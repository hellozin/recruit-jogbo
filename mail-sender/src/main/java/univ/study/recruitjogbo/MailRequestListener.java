package univ.study.recruitjogbo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailRequestListener {

    private static final String subject = "[RecruitJogbo] 가입 인증 메일입니다.";

    private final MailService mailService;

    @Value("${confirm.link}")
    String confirmLink;

    @RabbitListener(queues = "email.confirm")
    public void receiveMessage(final EmailConfirmRequestMessage message) throws MessagingException {
        String targetEmail = message.getTargetEmail();
        String confirmLink = message.getEmailConfirmLink();
        mailService.sendConfirmMail(targetEmail, subject, confirmLink);
        log.info("Send confirm mail to [{}]", targetEmail);
    }

}
