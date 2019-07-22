package univ.study.recruitjogbo;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class MailController {

    private final MailService mailService;

    @PostMapping("/mail")
    public MailRequest sendMail(@RequestBody MailRequest mailRequest) {
        System.out.println(mailRequest);
        mailService.sendSimpleMessage(mailRequest.getTo(), mailRequest.getSubject(), mailRequest.getText());
        return mailRequest;
    }

}
