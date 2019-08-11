package univ.study.recruitjogbo;

import lombok.Getter;

@Getter
public class ConfirmEmailTemplate {

    static final String subject = "[Recruit Jogbo] 사용자 인증 메일입니다.";

    static final String text = "이메일을 인증하기 위해 다음 링크를 클릭해 주세요.\n 링크: " +
            "http://localhost:8080/api/confirm/email?token=";

}
