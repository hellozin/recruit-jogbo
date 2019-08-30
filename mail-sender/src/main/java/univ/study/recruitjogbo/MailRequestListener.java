package univ.study.recruitjogbo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailRequestListener {

    private final MailService mailService;

    @RabbitListener(queues = "email.confirm")
    public void receiveMessage(final EmailConfirmRequestMessage message) throws MessagingException {
        String targetEmail = message.getTargetEmail();
        String subject = ConfirmEmailTemplate.subject;
        String token = ConfirmEmailTemplate.text + message.getEmailConfirmToken();
        mailService.sendConfirmMail(targetEmail, subject, token);
        log.info("Send confirm mail to [{}]", targetEmail);
    }

}
