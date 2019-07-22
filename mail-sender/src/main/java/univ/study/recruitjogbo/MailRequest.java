package univ.study.recruitjogbo;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class MailRequest {

    private String to;

    private String subject;

    private String text;

}
