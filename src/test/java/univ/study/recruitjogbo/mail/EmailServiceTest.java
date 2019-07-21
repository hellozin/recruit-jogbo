package univ.study.recruitjogbo.mail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(properties = {"spring.config.location=classpath:/google.yml,classpath:/mail.yml"})
public class EmailServiceTest {

    @Autowired
    EmailService emailService;

    @Test
    public void EmailTest() {
        emailService.sendSimpleMessage("paul5813@naver.com", "hello test", "hello hello.");
    }

}
