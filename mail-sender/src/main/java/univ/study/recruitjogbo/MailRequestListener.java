package univ.study.recruitjogbo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailRequestListener {

    private final MailService mailService;

    @RabbitListener(queues = "email.confirm")
    public void receiveMessage(final EmailConfirmRequestMessage message) {
        String targetEmail = message.getTargetEmail();
        String subject = ConfirmEmailTemplate.subject;
        String text = ConfirmEmailTemplate.text + message.getEmailConfirmToken();
        mailService.sendSimpleMessage(targetEmail, subject, text);
        log.info("Send confirm mail to [{}]", targetEmail);
    }

}
